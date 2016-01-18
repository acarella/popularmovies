package com.superflousjazzhands.popularmovies.model;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by TheGoodStuff on 1/16/16.
 */

public class TrailerColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    public static final String NAME = "name";

    @DataType(DataType.Type.TEXT)
    public static final String SIZE = "size";

    @DataType(DataType.Type.TEXT)
    public static final String SITE = "site";

    @DataType(DataType.Type.INTEGER)
    @References(table = MovieDatabase.MOVIES, column = TrailerColumns._ID)
    public static final String MOVIE_ID = "movie_id";

}
