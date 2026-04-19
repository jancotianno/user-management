package user_management.masking;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import user_management.annotation.MaskForRoles;

import java.util.List;

public class MaskingBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(
            SerializationConfig config,
            BeanDescription beanDesc,
            List<BeanPropertyWriter> beanProperties) {

        return beanProperties.stream()
                .map(writer -> {
                    if (writer.getAnnotation(MaskForRoles.class) != null) {
                        return new MaskingPropertyWriter(writer);
                    }
                    return writer;
                })
                .toList();
    }
}

