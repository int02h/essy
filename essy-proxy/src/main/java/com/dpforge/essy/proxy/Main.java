package com.dpforge.essy.proxy;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.head;
import static spark.Spark.post;
import static spark.Spark.put;

public class Main {

    public static void main(String[] args) {
        final ProxyRoute route = new ProxyRoute();
        get("/*", route);
        post("/*", route);
        put("/*", route);
        delete("/*", route);
        head("/*", route);
    }
}
