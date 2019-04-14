package org.dl4j.jdbc;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class ImageDaoImp implements ImageDao {

	private DataSource datasource;
	private JdbcTemplate jdbcTemplateObject;
	public void setdatasource(DataSource ds) {
		this.datasource=ds;
		this.jdbcTemplateObject=new JdbcTemplate(datasource);
	}

	public void addImage(Image image) {
		//String sql="insert into class.Image(ImagePath) values (?)";
		String sql="insert into image(ImagePath,InsertTime) VALUES(?,?)";
		jdbcTemplateObject.update(sql,image.getImagePath(),new Date());
	}

	public List<Image> getLast10() {
		List<Image> images=null;
		String sql="select ImagePath from image order by InsertTime Desc limit 10";
		images=jdbcTemplateObject.query(sql, new ImageMapper());
		return images;
	}

}
