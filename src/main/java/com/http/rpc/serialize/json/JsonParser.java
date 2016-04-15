package com.http.rpc.serialize.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.http.rpc.exception.RpcException;
import com.http.rpc.exception.RpcExceptionCodeEnum;
import com.http.rpc.serialize.Parser;
import com.http.rpc.serialize.Request;

/**
 * Created on 2015/8/17.
 */
public class JsonParser implements Parser
{
    private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);

    public static final Parser parser = new JsonParser();

    public Request reqParse(String param) throws RpcException {
        try
        {
            logger.debug("���ò��� {}", param);
            return (Request)JSON.parse(param);
        }
        catch (Exception e)
        {
            logger.error("ת���쳣 param = {}", param, e);
            throw new RpcException("",e, RpcExceptionCodeEnum.DATA_PARSER_ERROR.getCode(),param);
        }
    }

    public <T> T rsbParse(String result)
    {
        return (T)JSON.parse(result);
    }
}
