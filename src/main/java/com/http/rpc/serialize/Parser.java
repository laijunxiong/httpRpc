package com.http.rpc.serialize;

import com.http.rpc.exception.RpcException;

/**
 * Created on 2015/8/17.
 */
public interface Parser
{
    /**
     *
     * @param param �������
     * @return
     */
    Request reqParse(String param) throws RpcException;

    /**
     *
     * @param result
     * @return
     */
    public <T> T rsbParse(String result);
}
