package com.site.andrewsfood.Dao;

import com.site.andrewsfood.Model.domain.Miscellaneous;
import org.springframework.data.repository.CrudRepository;

public interface MiscellaneousRepo extends CrudRepository<Miscellaneous, Long> {
    Miscellaneous findById(long id);

}
