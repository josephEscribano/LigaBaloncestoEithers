module ClienteLiga {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.yaml.snakeyaml;
    requires lombok;
    requires io.vavr;
    requires retrofit2;
    requires okhttp3;
    requires retrofit2.converter.gson;
    requires com.google.gson;
    requires org.apache.logging.log4j;
    requires javafx.graphics;
    requires jakarta.enterprise.cdi.api;
    requires jakarta.inject.api;
    requires retrofit2.converter.scalars;
    requires io.reactivex.rxjava3;
    requires retrofit2.adapter.rxjava3;
    requires Common;
    requires org.pdfsam.rxjavafx;

    opens quevedo.ClienteLiga.gui;
    opens quevedo.ClienteLiga.dao.cookie;
    opens quevedo.ClienteLiga.gui.controllers;
    opens quevedo.ClienteLiga.gui.utils;
    opens quevedo.ClienteLiga.config;
    opens quevedo.ClienteLiga.service;
    opens quevedo.ClienteLiga.dao;
    opens quevedo.ClienteLiga.dao.retrofit;
    opens quevedo.ClienteLiga.dao.utils;

    exports quevedo.ClienteLiga.gui;
    exports quevedo.ClienteLiga.dao;
    exports quevedo.ClienteLiga.dao.cookie;
    exports quevedo.ClienteLiga.gui.controllers;
    exports quevedo.ClienteLiga.gui.utils;
    exports quevedo.ClienteLiga.config;
    exports quevedo.ClienteLiga.gui.controllers.pantallasAcciones;
    opens quevedo.ClienteLiga.gui.controllers.pantallasAcciones;
    exports quevedo.ClienteLiga.service;
    exports quevedo.ClienteLiga.dao.retrofit;
    exports quevedo.ClienteLiga.dao.utils;
}