package com.locarie.backend.mapper;

import java.util.Collection;
import org.modelmapper.Converter;

public interface Mapper<A, B> {
  B mapTo(A a);

  A mapFrom(B b);

  Converter<Collection<?>, Integer> COLLECTION_TO_SIZE_CONVERTER =
      c -> {
        if (c.getSource() == null) {
          return 0;
        }
        return c.getSource().size();
      };
}
