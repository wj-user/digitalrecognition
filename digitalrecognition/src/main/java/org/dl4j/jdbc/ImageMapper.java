package org.dl4j.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ImageMapper implements RowMapper<Image> {

	public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
		Image image=new Image();
		image.setImagePath(rs.getString("ImagePath"));
		return image;
	}

}
