package com.graphaware;

import java.util.List;
import java.util.Optional;

public interface MovieAPI {

    void create(Movie movie);

    List<Movie> getAll();

    void deleteAll();
}
