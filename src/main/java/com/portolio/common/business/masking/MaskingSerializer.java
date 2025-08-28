package com.portolio.common.business.masking;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * @Masking 어노테이션의 실제 동작을 처리하는 커스텀 Jackson Serializer 입니다.
 * 이 클래스는 객체의 특정 필드를 JSON 문자열로 변환(직렬화)할 때,
 * 일반적인 값 변환 대신 마스킹 로직을 적용하는 역할을 합니다.
 *
 * ContextualSerializer 인터페이스를 구현하는 것이 핵심입니다.
 * 이를 통해 필드에 붙은 @Masking 어노테이션의 속성값(어떤 마스킹 타입을 쓸지)을 런타임에 읽어와
 * 동적으로 다른 마스킹 로직을 적용할 수 있습니다.
 */
public class MaskingSerializer extends StdSerializer<String> implements ContextualSerializer {

    /**
     * 이 Serializer 인스턴스가 어떤 종류의 마스킹을 수행할지를 저장하는 필드입니다.
     * createContextual() 메서드를 통해 이 필드의 값이 결정됩니다.
     */
    private MaskingType maskingType;

    /**
     * ✨ 해결: 파라미터 없는 생성자를 직접 작성합니다.
     * StdSerializer를 상속받기 때문에, 상위 클래스의 생성자 super(Class<T>)를 호출해줘야 합니다.
     * 이 생성자는 Jackson이 초기 인스턴스를 만들 때 사용될 수 있습니다.
     */
    public MaskingSerializer(){
        super(String.class);
    }

    /**
     * createContextual 메서드에서 마스킹 타입이 결정되었을 때 호출되는 생성자입니다.
     * @param maskingType 이 Serializer가 사용할 마스킹 타입(NAME, EMAIL 등).
     */
    public MaskingSerializer(MaskingType maskingType){
        super(String.class);
        this.maskingType = maskingType;
    }

    /**
     * 이 Serializer의 가장 중요한 부분입니다.
     * Jackson이 필드를 직렬화하기 직전에 호출되어, 해당 필드의 컨텍스트(어떤 어노테이션이 붙었는지 등)를 분석할 기회를 줍니다.
     * 이 메서드의 반환값으로 실제 직렬화에 사용될 Serializer 인스턴스가 결정됩니다.
     *
     * @param prov      SerializerProvider, 다른 Serializer를 찾거나 할 때 사용됩니다.
     * @param property  현재 처리 중인 필드(프로퍼티)의 정보. 이 객체를 통해 어노테이션을 조회할 수 있습니다.
     * @return 실제 직렬화에 사용될 JsonSerializer 인스턴스.
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        // 1. 방어 코드: 프로퍼티 정보가 없는 경우, 기본 null 처리 Serializer를 반환합니다.
        if (property == null) {
            return prov.findNullValueSerializer(null);
        }

        // 2. 현재 처리 중인 필드에서 @Masking 어노테이션을 찾습니다.
        Masking maskingAnnotation = property.getAnnotation(Masking.class);

        // 3. @Masking 어노테이션이 존재하는지 확인합니다.
        if (Objects.nonNull(maskingAnnotation)) {
            // 4. 어노테이션이 있다면, 어노테이션의 value() 속성(예: MaskingType.NAME)을 가져옵니다.
            //    그리고 그 값을 가진 '새로운' MaskingSerializer 인스턴스를 생성하여 반환합니다.
            //    이제 이 새로 생성된 인스턴스가 실제 serialize() 메서드를 호출하게 됩니다.
            return new MaskingSerializer(maskingAnnotation.value());
        }

        // 5. @Masking 어노테이션이 없다면, 이 커스텀 Serializer는 필요 없으므로
        //    Jackson의 기본 Serializer를 찾아서 반환하도록 위임합니다.
        return prov.findValueSerializer(property.getType(), property);
    }

    /**
     * 실제 직렬화(마스킹) 로직을 수행하는 메서드입니다.
     * createContextual()에서 반환된 Serializer 인스턴스가 이 메서드를 호출합니다.
     *
     * @param value      필드의 원본 값 (e.g., "홍길동").
     * @param gen        JSON을 출력하는 JsonGenerator.
     * @param provider   SerializerProvider.
     * @throws IOException
     */
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // 1. maskingType 필드가 null이 아닌지 확인합니다.
        //    (createContextual에서 @Masking이 발견되어 new MaskingSerializer(type)이 호출된 경우)
        if (Objects.nonNull(maskingType)) {
            // 2. maskingType에 정의된 mask() 메서드를 호출하여 원본 값을 마스킹 처리합니다.
            // 3. 마스킹된 값을 JSON 문자열로 출력합니다. (gen.writeString(...))
            gen.writeString(maskingType.mask(value));
        } else {
            // 4. maskingType이 null이라면(createContextual에서 @Masking을 못 찾은 경우),
            //    아무 처리도 하지 않고 원본 값을 그대로 JSON 문자열로 출력합니다.
            gen.writeString(value);
        }
    }
}
