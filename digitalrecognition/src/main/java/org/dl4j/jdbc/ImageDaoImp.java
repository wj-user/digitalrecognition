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
		String sql="insert into image(ImagePath,Base64,InsertTime) VALUES(?,?,?)";
		jdbcTemplateObject.update(sql,image.getImagePath(),image.getBase64(),new Date());
	}

	public List<Image> getLast10() {
		List<Image> images=null;
		String sql="select Base64 from image order by InsertTime Desc limit 10";
		images=jdbcTemplateObject.query(sql, new ImageMapper());
		return images;
	}

}
