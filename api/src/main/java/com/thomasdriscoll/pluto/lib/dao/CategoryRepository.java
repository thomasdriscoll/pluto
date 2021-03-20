package com.thomasdriscoll.pluto.lib.dao;

import com.thomasdriscoll.pluto.lib.models.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryDao, Long> {
    List<CategoryDao> findByUserId(String userId);
    List<CategoryDao> findByUserIdAndParentCategory(String userId, String parent);
}
