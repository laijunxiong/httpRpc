package com.http.rpc.zookeeper;


import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.Watcher;

import com.http.rpc.exception.RpcException;
import com.http.rpc.exception.RpcExceptionCodeEnum;

/**
 * Created by version_z on 2015/8/23.
 */
public class ZookeeperClient
{
    private ZkClient zkClient;

    private volatile Watcher.Event.KeeperState state = Watcher.Event.KeeperState.SyncConnected;

    public ZookeeperClient(String url)
    {
        zkClient = new ZkClient(url);
    }
    //创建持久化目录
    public void createPersistent(String path)
    {
        try {
            zkClient.createPersistent(path, true);
        } catch (ZkNodeExistsException e) {
        }
    }
    //创建临时目录
    public void createEphemeral(String path,String data)
    {
        try {
            zkClient.createEphemeralSequential(path, data);
        } catch (ZkNodeExistsException e) {
        }
    }
    //创建临时目录
    public void deleteEphemeral(String path)
    {
        try {
            zkClient.delete(path);
        } catch (ZkNodeExistsException e) {
        }
    }
    //获取子目录
    public  List<String> getChildren(String path) throws RpcException {
        try {
            List<String> pathList = zkClient.getChildren(path);
            if (pathList != null && pathList.size() > 0)
            {
                return pathList;
            }
        } catch (ZkNoNodeException e) {
            throw new RpcException(e.getMessage(),e,RpcExceptionCodeEnum.NO_PROVIDERS.getCode(),path);
        }
        throw new RpcException(RpcExceptionCodeEnum.NO_PROVIDERS.getCode(),path);
    }
    //获取节点中的值
    public <T> T getData(String path)
    {
        try {
            return zkClient.readData(path, true);
        } catch (ZkNoNodeException e) {
            return null;
        }
    }

    public void delete(String path)
    {
        try {
            zkClient.delete(path);
        } catch (ZkNodeExistsException e) {
        }
    }

    public void setWatcher(String path,IZkChildListener watcher)
    {
        zkClient.subscribeChildChanges(path, watcher);
    }

    public boolean isConnected() {
        return state == Watcher.Event.KeeperState.SyncConnected;
    }

    public void doClose() {
        zkClient.close();
    }

}
