package com.http.rpc.invoke;

import java.io.OutputStream;

import com.http.rpc.exception.RpcException;

/**
 * Created by version_z on 2015/8/22.
 */
public interface Invoker
{
    /**
     * ��������
     * @param request ������
     * @param url ����������
     * @return
     * @throws RpcException
     */
    String request(String request,String url) throws RpcException;

    /**
     * ����Ӧ��
     * @param response ��Ӧ����
     * @param outputStream �����
     * @throws RpcException
     */
    void response(String response,OutputStream outputStream) throws RpcException;
}
