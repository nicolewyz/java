package com.jsoniter.any;

import com.jsoniter.*;
import com.jsoniter.spi.TypeLiteral;

import java.io.IOException;
import java.util.*;

abstract class LazyAny extends Any {

    final static Set<String> EMPTY_KEYS = Collections.unmodifiableSet(new HashSet<String>());
    final static Iterator<Any> EMPTY_ITERATOR = new Iterator<Any>() {
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Any next() {
            throw new UnsupportedOperationException();
        }
    };
    protected final static ThreadLocal<JsonIterator> tlsIter = new ThreadLocal<JsonIterator>() {
        @Override
        protected JsonIterator initialValue() {
            return new JsonIterator();
        }
    };
    protected final byte[] data;
    protected final int head;
    protected final int tail;

    public LazyAny(byte[] data, int head, int tail) {
        this.data = data;
        this.head = head;
        this.tail = tail;
    }

    public abstract ValueType valueType();

    public final <T> T bindTo(T obj, Object... keys) {
        Any found = get(keys);
        if (found == null) {
            return null;
        }
        return found.bindTo(obj);
    }

    public final <T> T bindTo(T obj) {
        try {
            return parse().read(obj);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public final <T> T bindTo(TypeLiteral<T> typeLiteral, T obj, Object... keys) {
        Any found = get(keys);
        if (found == null) {
            return null;
        }
        return found.bindTo(typeLiteral, obj);
    }

    public final <T> T bindTo(TypeLiteral<T> typeLiteral, T obj) {
        try {
            return parse().read(typeLiteral, obj);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public final Object object(Object... keys) {
        Any found = get(keys);
        if (found == null) {
            return null;
        }
        return found.object();
    }

    public abstract Object object();

    public final <T> T as(Class<T> clazz, Object... keys) {
        Any found = get(keys);
        if (found == null) {
            return null;
        }
        return found.as(clazz);
    }

    public final <T> T as(Class<T> clazz) {
        try {
            return parse().read(clazz);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public final <T> T as(TypeLiteral<T> typeLiteral, Object... keys) {
        Any found = get(keys);
        if (found == null) {
            return null;
        }
        return found.as(typeLiteral);
    }

    public final <T> T as(TypeLiteral<T> typeLiteral) {
        try {
            return parse().read(typeLiteral);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public final boolean toBoolean(Object... keys) {
        Any found = get(keys);
        if (found == null) {
            return false;
        }
        return found.toBoolean();
    }

    public boolean toBoolean() {
        throw reportUnexpectedType(ValueType.BOOLEAN);
    }

    public final int toInt(Object... keys) {
        Any found = get(keys);
        if (found == null) {
            return 0;
        }
        return found.toInt();
    }

    public int toInt() {
        throw reportUnexpectedType(ValueType.NUMBER);
    }

    public final long toLong(Object... keys) {
        Any found = get(keys);
        if (found == null) {
            return 0;
        }
        return found.toLong();
    }

    public long toLong() {
        throw reportUnexpectedType(ValueType.NUMBER);
    }

    public final float toFloat(Object... keys) {
        Any found = get(keys);
        if (found == null) {
            return 0;
        }
        return found.toFloat();
    }

    public float toFloat() {
        throw reportUnexpectedType(ValueType.NUMBER);
    }

    public final double toDouble(Object... keys) {
        Any found = get(keys);
        if (found == null) {
            return 0;
        }
        return found.toDouble();
    }

    public double toDouble() {
        throw reportUnexpectedType(ValueType.NUMBER);
    }

    public final String toString(Object... keys) {
        Any found = get(keys);
        if (found == null) {
            return null;
        }
        return found.toString();
    }

    public String toString() {
        return new String(data, head, tail - head);
    }

    public int size() {
        return 0;
    }

    public Set<String> keys() {
        return EMPTY_KEYS;
    }

    @Override
    public Iterator<Any> iterator() {
        return EMPTY_ITERATOR;
    }

    public Any get(int index) {
        return null;
    }

    public Any get(Object key) {
        return null;
    }

    public final JsonIterator parse() {
        JsonIterator iter = tlsIter.get();
        iter.reset(data, head, tail);
        return iter;
    }
}
