package com.project.FirstProject.mappers;

public interface Mapper<A, B> {

    B mapto(A a);

    A mapFrom(B b);
}
