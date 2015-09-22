
package com.makcsv.css;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Max Soloviov <makcsv@bigmir.net>
 */
public class CssProperty {
    
    private String _propertyText;
    private String _propertyName;
    private String _propertyValue;
    
    public CssProperty(String propertyText) {
        
        this._propertyText = propertyText;
        
        this._parseProperty();
        
    }
    
    public CssProperty(String propertyName, String propertyValue) {
        
        this._propertyName = propertyName;
        this._propertyValue = propertyValue;
        this._propertyText = propertyName + ": " + propertyValue + ";";
        
    }
    
    private void _parseProperty() {
        
        int colonPos = this._propertyText.indexOf(":");
        int endPos = (this._propertyText.contains(";")) ? this._propertyText.indexOf(";") : this._propertyText.length();
        
        if (colonPos == -1) {
            
            this._propertyName = "";
            this._propertyValue = this._propertyText.substring(colonPos + 1, endPos).trim();
            
        } else {
            
            this._propertyName = this._propertyText.substring(0, colonPos).trim();
            this._propertyValue = this._propertyText.substring(colonPos + 1, endPos).trim();
            
        }
        
    }
    
    private List<CssProperty> _decomposeBackgroundProperty() {
        
        List<CssProperty> properties = new ArrayList<>();
        
        String[] params = this._propertyValue.split(" ");
        String url = "", repeat = "", color = "", attachment = "", position = "";
        
        for (String param : params) {

            if (param.contains("url")) {
                
                url = param;

            } else if (param.contains("repeat")) {

                repeat = param;

            } else if (param.contains("#") || param.contains("rgb") || param.contains("transparent")) {

                color = param;

            } else if (param.contains("fixed") || param.contains("scroll") || param.contains("local")) {

                attachment = param;

            } else if (param.contains("left") || param.contains("top") || param.contains("right") || param.contains("bottom") || param.contains("%")) {

                position += " " + param;

            }

        }
        
        if (!url.isEmpty()) {
            properties.add(new CssProperty("background-image", url));
        }
        
        if (!repeat.isEmpty()) {
            properties.add(new CssProperty("background-repeat", repeat));
        }
        
        if (!color.isEmpty()) {
            properties.add(new CssProperty("background-color", color));
        }
        
        if (!attachment.isEmpty()) {
            properties.add(new CssProperty("background-attachment", attachment));
        }
        
        if (!position.isEmpty()) {
            properties.add(new CssProperty("background-position", position));
        }
        
        return properties;
        
    }
    
    public String asText() {
        
        return this._propertyText;
        
    }
    
    public String getName() {
        
        return this._propertyName;
        
    }
    
    public String getValue() {
        
        return this._propertyValue;
        
    }
    
    public List<CssProperty> decompose() {
        
        List<CssProperty> properties = new ArrayList<>();
        
        switch (this._propertyName) {
            
            case "background":
                properties.addAll(this._decomposeBackgroundProperty());
                break;
                
            default:
                properties.add(this);
                break;
            
        }
        
        return properties;
        
    }
    
}