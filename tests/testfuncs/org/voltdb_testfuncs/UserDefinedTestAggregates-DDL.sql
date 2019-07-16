DROP AGGREGATE FUNCTION uaddstr IF EXISTS;
DROP AGGREGATE FUNCTION uavg IF EXISTS;
DROP AGGREGATE FUNCTION ucount IF EXISTS;
DROP AGGREGATE FUNCTION umax IF EXISTS;
DROP AGGREGATE FUNCTION umedian IF EXISTS;
DROP AGGREGATE FUNCTION umin IF EXISTS;
DROP AGGREGATE FUNCTION umode IF EXISTS;
DROP AGGREGATE FUNCTION uprimesum IF EXISTS;
DROP AGGREGATE FUNCTION usum IF EXISTS;

CREATE AGGREGATE FUNCTION uaddstr FROM CLASS org.voltdb_testfuncs.Uaddstr;
CREATE AGGREGATE FUNCTION uavg FROM CLASS org.voltdb_testfuncs.Uavg;
CREATE AGGREGATE FUNCTION ucount FROM CLASS org.voltdb_testfuncs.Ucount;
CREATE AGGREGATE FUNCTION umax FROM CLASS org.voltdb_testfuncs.Umax;
CREATE AGGREGATE FUNCTION umedian FROM CLASS org.voltdb_testfuncs.Umedian;
CREATE AGGREGATE FUNCTION umin FROM CLASS org.voltdb_testfuncs.Umin;
CREATE AGGREGATE FUNCTION umode FROM CLASS org.voltdb_testfuncs.Umode;
CREATE AGGREGATE FUNCTION uprimesum FROM CLASS org.voltdb_testfuncs.Uprimesum;
CREATE AGGREGATE FUNCTION usum FROM CLASS org.voltdb_testfuncs.Usum;