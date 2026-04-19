package user_management.masking;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import user_management.annotation.MaskForRoles;

import java.util.Set;
import java.util.stream.Collectors;

public class MaskingPropertyWriter extends BeanPropertyWriter {

    public MaskingPropertyWriter(BeanPropertyWriter base) {
        super(base);
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
            throws Exception {

        // Recupera l'annotazione dal campo
        MaskForRoles annotation = this.getAnnotation(MaskForRoles.class);

        if (annotation != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null) {
                Set<String> roles = auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());

                for (String role : annotation.value()) {
                    if (roles.contains("ROLE_" + role)) {
                        gen.writeStringField(getName(), "******");
                        return;
                    }
                }
            }
        }

        super.serializeAsField(bean, gen, prov);
    }
}


