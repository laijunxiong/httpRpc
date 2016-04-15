package com.http.rpc.proxy;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.http.rpc.container.Container;
import com.http.rpc.container.HttpContainer;
import com.http.rpc.exception.RpcException;
import com.http.rpc.exception.RpcExceptionCodeEnum;
import com.http.rpc.invoke.HttpInvoker;
import com.http.rpc.invoke.Invoker;
import com.http.rpc.invoke.ProviderConfig;
import com.http.rpc.serialize.Formater;
import com.http.rpc.serialize.Parser;
import com.http.rpc.serialize.Request;
import com.http.rpc.serialize.json.JsonFormater;
import com.http.rpc.serialize.json.JsonParser;

/**
 * Created by version_z on 2015/8/22.
 */
public class ProviderProxyFactory extends AbstractHandler
{
    private static final Logger logger = LoggerFactory.getLogger(ProviderProxyFactory.class);

    private Map<Class,Object> providers = new ConcurrentHashMap<Class, Object>();

    private static ProviderProxyFactory factory;

    private Parser parser = JsonParser.parser;

    private Formater formater = JsonFormater.formater;

    private Invoker invoker = HttpInvoker.invoker;

    public ProviderProxyFactory(Map<Class,Object> providers)
    {
        if (Container.container == null)
        {
            new HttpContainer(this).start();
        }
        for (Map.Entry<Class,Object> entry: providers.entrySet())
        {
            register(entry.getKey(), entry.getValue(),null);
        }
        factory = this;
    }

    public ProviderProxyFactory(Map<Class,Object> providers,ProviderConfig providerConfig)
    {
        if (Container.container == null)
        {
            new HttpContainer(this,providerConfig).start();
        }
        for (Map.Entry<Class,Object> entry: providers.entrySet())
        {
            register(entry.getKey(), entry.getValue(),providerConfig);
        }
        factory = this;
    }

    public void register(Class clazz,Object object,ProviderConfig config)
    {
        providers.put(clazz, object);
        if (config != null){
            config.register(clazz);
        }
        logger.info("{} 已经发布", clazz.getSimpleName());
    }


    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException
    {
        String reqStr = request.getParameter("data");
        try
        {
            //将请求参数解析
            Request rpcRequest = parser.reqParse(reqStr);
            //反射请求
            Object result = rpcRequest.invoke(ProviderProxyFactory.getInstance().getBeanByClass(rpcRequest.getClazz()));
            //相应请求
            invoker.response(formater.rsbFormat(result),response.getOutputStream());
        }
        catch (RpcException e)
        {
            e.printStackTrace();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }






    public Object getBeanByClass(Class clazz) throws RpcException
    {
        Object bean =  providers.get(clazz);
        if (bean != null)
        {
            return bean;
        }
        throw new RpcException(RpcExceptionCodeEnum.NO_BEAN_FOUND.getCode(),clazz);
    }

    public static ProviderProxyFactory getInstance()
    {
        return factory;
    }




}
