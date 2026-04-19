package user_management.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user_management.masking.MaskingBeanSerializerModifier;

@Configuration
public class JacksonMaskingConfig {

    @Bean
    public SimpleModule maskingModule() {
        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new MaskingBeanSerializerModifier());
        return module;
    }
}

