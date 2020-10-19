package com.sample.springboot.cache.redis.orika.converter;

import com.sample.springboot.cache.redis.enums.Position;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class EnumsConverters {

    public static class PositionToIntegerConverter extends BidirectionalConverter<Position, Integer> {

        @Override
        public Integer convertTo(Position source, Type<Integer> destinationType) {
            if (null == source) {
                return null;
            }
            return source.value();
        }

        @Override
        public Position convertFrom(Integer source, Type<Position> destinationType) {
            if (null == source) {
                return null;
            }

            try {
                return Position.valueOf(source);
            } catch (Exception e) {
                return null;
            }
        }
    }

}
