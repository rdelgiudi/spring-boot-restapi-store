package com.delgiudice.restapistore.mappers;

// Mapper interface for mapping object A to object B
public interface Mapper<A, B>{

    B mapTo(A a);

    A mapFrom(B b);
}
