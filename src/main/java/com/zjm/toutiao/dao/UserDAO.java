package com.zjm.toutiao.dao;

import com.zjm.toutiao.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDAO {
        String TABLE_NAME = "user";
        String INSERT_FIELDS = " name, password, salt, head_url, active";
        String SELECT_FIELDS = " id,"+INSERT_FIELDS;

        @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
                ") values (#{name},#{password},#{salt},#{headUrl},#{active})"})
        int addUser(User user);

        @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
        User selectById(int id);

        @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
        User selectByName(String name);

        @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
        void updatePassword(User user);

        @Update({"update ", TABLE_NAME, "set active=#{active} where id=#{id}"})
        void updateActive(User user);

        @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
        void deleteById(int id);
}
