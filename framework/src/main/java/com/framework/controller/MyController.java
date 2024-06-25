package com.framework.controllers;

public class MyController {

    public ModelView submitName(@Param(name = "name") String name) {
        ModelView modelView = new ModelView();
        modelView.setViewName("displayName");
        modelView.addObject("name", name);
        return modelView;
    }
}
