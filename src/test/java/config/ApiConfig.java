package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:auth.properties")
public interface ApiConfig extends Config {
    @Config.Key("EMAIL")
    String email();

    @Key("PASSWORD")
    String password();


}

