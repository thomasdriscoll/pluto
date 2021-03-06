package com.thomasdriscoll.pluto.lib.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryDao, Long> {
    List<CategoryDao> findByUserId(String userId);
    List<CategoryDao> findByUserIdAndParentCategory(String userId, String parent);
    CategoryDao findByUserIdAndCategoryName(String userId, String categoryName);
    void deleteByUserIdAndCategoryName(String userId, String categoryName);
}
