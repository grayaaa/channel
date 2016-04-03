package com.netease.channel.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.*;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.*;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.LineParser;
import org.apache.http.util.CharArrayBuffer;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * HTTP工具类
 */
public class HttpClientUtil {
	
	private static final Log logger = LogFactory.getLog(HttpClientUtil.class);
	
	private static final int socketTimeoutTime = 5000;
	private static final int connectTimeoutTime = 5000;
	private static final int connectionRequestTimeoutTime = 5000;
	private static HttpClientUtil httpClientUtil = new HttpClientUtil();
	
	private CloseableHttpClient httpclient;
	
	public HttpClientUtil() {
		// Use custom message parser / writer to customize the way HTTP
        // messages are parsed from and written out to the data stream.
        HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

            @Override
            public HttpMessageParser<HttpResponse> create(
                SessionInputBuffer buffer, MessageConstraints constraints) {
                LineParser lineParser = new BasicLineParser() {

                    @Override
                    public Header parseHeader(final CharArrayBuffer buffer) {
                        try {
                            return super.parseHeader(buffer);
                        } catch (ParseException ex) {
                            return new BasicHeader(buffer.toString(), null);
                        }
                    }

                };
                return new DefaultHttpResponseParser(
                    buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {

                    @Override
                    protected boolean reject(final CharArrayBuffer line, int count) {
                        // try to ignore all garbage preceding a status line infinitely
                        return false;
                    }

                };
            }

        };
		
		HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

        // Use a custom connection factory to customize the process of
        // initialization of outgoing HTTP connections. Beside standard connection
        // configuration parameters HTTP connection factory can define message
        // parser / writer routines to be employed by individual connections.
        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                requestWriterFactory, responseParserFactory);
		
		SSLContext sslcontext = SSLContexts.createSystemDefault();
	    // Use custom hostname verifier to customize SSL hostname verification.
	    X509HostnameVerifier hostnameVerifier = new BrowserCompatHostnameVerifier();
		
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.INSTANCE)
	            .register("https", new SSLConnectionSocketFactory(sslcontext, hostnameVerifier))
	            .build();
		
		 // Use custom DNS resolver to override the system DNS resolution.
        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                if (host.equalsIgnoreCase("myhost")) {
                    return new InetAddress[] { InetAddress.getByAddress(new byte[] {127, 0, 0, 1}) };
                } else {
                    return super.resolve(host);
                }
            }

        };
		
        // Use custom cookie store if necessary.
        CookieStore cookieStore = new BasicCookieStore();
        // Use custom credentials provider if necessary.
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // Create global request configuration
        RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setCookieSpec(CookieSpecs.BEST_MATCH)
            .setExpectContinueEnabled(true)
            .setStaleConnectionCheckEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
            .setSocketTimeout(socketTimeoutTime)
            .setConnectTimeout(connectTimeoutTime)
            .setConnectionRequestTimeout(connectionRequestTimeoutTime)
            .build();

        
      	httpclient = HttpClients.custom()
                .setConnectionManager(createConnectionManager(socketFactoryRegistry,connFactory,dnsResolver))
                .setDefaultCookieStore(cookieStore)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
	}
	
	private PoolingHttpClientConnectionManager createConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry,
			HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory,
			DnsResolver dnsResolver) {
		// Create a connection manager with custom configuration.
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, connFactory, dnsResolver);

        // Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
            .setTcpNoDelay(true)
            .build();
        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(socketConfig);

        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
            .setMaxHeaderCount(200)
            .setMaxLineLength(2000)
            .build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .setMessageConstraints(messageConstraints)
            .build();
        // Configure the connection manager to use connection configuration either
        // by default or for a specific host.
        connManager.setDefaultConnectionConfig(connectionConfig);

        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(10);
        return connManager;
	}
	
	public static HttpClientUtil getInstance() {
		return httpClientUtil;
	}
	
	public String execute(String url) {
		String ret = null;
		HttpGet httpget = new HttpGet(url);
		// Execution context can be customized locally.
		HttpClientContext context = HttpClientContext.create();
     
		logger.info("executing request " + httpget.getURI());
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpget, context);
			HttpEntity entity = response.getEntity();
			logger.info("StatusLine:" + response.getStatusLine());
			if (entity != null) {
				ret = IOUtils.toString(entity.getContent());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return ret;
	}
	
	public String execute(String url, NameValuePair... params) {
		String ret = null;
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		
		for(NameValuePair param : params) {
			formparams.add(param);
		}
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		httppost.setEntity(formEntity);
		// Execution context can be customized locally.
		HttpClientContext context = HttpClientContext.create();
     
		logger.info("executing request " + httppost.getURI());
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httppost, context);
			HttpEntity entity = response.getEntity();
			logger.info("StatusLine:" + response.getStatusLine());
			if (entity != null) {
				ret = IOUtils.toString(entity.getContent());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return ret;
	}
	
	public String execute(String url, HttpEntity httpEntity) {
		String ret = null;
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(httpEntity);
		logger.info("executing request " + httppost.getURI());
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			logger.info("StatusLine:" + response.getStatusLine());
			if (entity != null) {
				ret = IOUtils.toString(entity.getContent());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return ret;
	}
	
	public String execute(String url, HttpEntity httpEntity, Header[] headers) {
		String ret = null;
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(httpEntity);
		httppost.setHeaders(headers);
		logger.info("executing request " + httppost.getURI());
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			logger.info("StatusLine:" + response.getStatusLine());
			if (entity != null) {
				ret = IOUtils.toString(entity.getContent());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return ret;
	}
	
	public static void main(String[] args) {
		HttpClientUtil httpClientUtil = getInstance();
		
		String url = "http://extapi.live.netease.com/api/live/create";
		String ret = httpClientUtil.execute(url, new BasicNameValuePair("tag", String.valueOf(1389095820)),
				new BasicNameValuePair("roomId", String.valueOf(100015)));
		System.out.println(ret);

		url = "http://extapi.live.netease.com/api/live/cancel?tag=1389095820&roomId=100015";
		ret = httpClientUtil.execute(url);
		System.out.println(ret);
		
	}
	

}
