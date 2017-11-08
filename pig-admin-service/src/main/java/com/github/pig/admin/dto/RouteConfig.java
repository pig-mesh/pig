package com.github.pig.admin.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * @author lengleng
 * @date 2017/11/7
 */
public class RouteConfig implements Serializable{

    @com.alibaba.fastjson.annotation.JSONField(name = "path")
    private String path;
    @com.alibaba.fastjson.annotation.JSONField(name = "component")
    private String component;
    @com.alibaba.fastjson.annotation.JSONField(name = "name")
    private String name;
    @com.alibaba.fastjson.annotation.JSONField(name = "components")
    private String components;
    @com.alibaba.fastjson.annotation.JSONField(name = "redirect")
    private String redirect;
    @com.alibaba.fastjson.annotation.JSONField(name = "props")
    private String props;
    @com.alibaba.fastjson.annotation.JSONField(name = "alias")
    private String alias;
    @com.alibaba.fastjson.annotation.JSONField(name = "children")
    private String children;
    @com.alibaba.fastjson.annotation.JSONField(name = "beforeEnter")
    private String beforeEnter;
    @com.alibaba.fastjson.annotation.JSONField(name = "meta")
    private Map<String,String> meta;
    @com.alibaba.fastjson.annotation.JSONField(name = "caseSensitive")
    private Boolean caseSensitive;
    @com.alibaba.fastjson.annotation.JSONField(name = "pathToRegexpOptions")
    private String pathToRegexpOptions;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponents() {
        return components;
    }

    public void setComponents(String components) {
        this.components = components;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getBeforeEnter() {
        return beforeEnter;
    }

    public void setBeforeEnter(String beforeEnter) {
        this.beforeEnter = beforeEnter;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public Boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(Boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public String getPathToRegexpOptions() {
        return pathToRegexpOptions;
    }

    public void setPathToRegexpOptions(String pathToRegexpOptions) {
        this.pathToRegexpOptions = pathToRegexpOptions;
    }
}
