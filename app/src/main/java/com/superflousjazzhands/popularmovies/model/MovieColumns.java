package com.superflousjazzhands.popularmovies.model;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import java.util.List;


/**
 * Created by TheGoodStuff on 1/16/16.
 */

public class MovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    public static final String TITLE = "title";

    @DataType(DataType.Type.TEXT)
    public static final String POSTER_URL = "poster_url";

    @DataType(DataType.Type.TEXT)
    public static final String SYNOPSIS = "synopsis";

    @DataType(DataType.Type.TEXT)
    public static final String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.TEXT)
    public static final String RUNTIME = "runtime";

    @DataType(DataType.Type.TEXT)
    public static final String SERVER_ID = "server_Id";

    @DataType(DataType.Type.REAL)
    public static final String USER_RATING = "user_rating";
}
