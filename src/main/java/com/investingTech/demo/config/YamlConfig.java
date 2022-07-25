package com.investingTech.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YamlConfig {

    private String get_price_url;
    private String get_ticker_url;

    private String AutoCompleteStock_url;
    private String portfolio_filePath;
    private String ticker_filePath;
    private String networthTracking_filePath;

    public String getGet_price_url() {
        return get_price_url;
    }

    public void setGet_price_url(String get_price_url) {
        this.get_price_url = get_price_url;
    }

    public String getGet_ticker_url() {
        return get_ticker_url;
    }

    public void setGet_ticker_url(String get_ticker_url) {
        this.get_ticker_url = get_ticker_url;
    }

    public String getPortfolio_filePath() {
        return portfolio_filePath;
    }

    public void setPortfolio_filePath(String portfolio_filePath) {
        this.portfolio_filePath = portfolio_filePath;
    }

    public String getTicker_filePath() {
        return ticker_filePath;
    }

    public void setTicker_filePath(String ticker_filePath) {
        this.ticker_filePath = ticker_filePath;
    }

    public String getNetworthTracking_filePath() {
        return networthTracking_filePath;
    }

    public void setNetworthTracking_filePath(String networthTracking_filePath) {
        this.networthTracking_filePath = networthTracking_filePath;
    }

    public String getAutoCompleteStock_url() {
        return AutoCompleteStock_url;
    }

    public void setAutoCompleteStock_url(String autoCompleteStock_url) {
        AutoCompleteStock_url = autoCompleteStock_url;
    }

    @Bean("YamlConfiguration")
    public YamlConfig yamlConfig(){
        return new YamlConfig();
    }
}
