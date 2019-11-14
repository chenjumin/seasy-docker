package com.seasy.docker.common.thrift.loader;

/**
 * 加载器
 */
public interface Loader<T> {
    T load();
}
