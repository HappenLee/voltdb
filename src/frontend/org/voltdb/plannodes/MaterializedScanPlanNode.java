/* This file is part of VoltDB.
 * Copyright (C) 2008-2013 VoltDB Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with VoltDB.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.voltdb.plannodes;

import java.util.ArrayList;
import java.util.List;

import org.json_voltpatches.JSONArray;
import org.json_voltpatches.JSONException;
import org.json_voltpatches.JSONObject;
import org.json_voltpatches.JSONStringer;
import org.voltdb.catalog.Cluster;
import org.voltdb.catalog.Database;
import org.voltdb.compiler.DatabaseEstimates;
import org.voltdb.compiler.ScalarValueHints;
import org.voltdb.expressions.AbstractExpression;
import org.voltdb.types.PlanNodeType;

/**
 * Used for SQL-IN that are accelerated with indexes.
 * A MaterializedScanPlanNode is created from the list part
 * of the SQL-IN-LIST. It is inner-joined with NLIJ to another
 * table to make the SQL-IN fast.
 *
 */
public class MaterializedScanPlanNode extends AbstractPlanNode {

    protected List<AbstractExpression> m_tableData = new ArrayList<AbstractExpression>();

    public enum Members {
        TABLE_DATA;
    }

    public MaterializedScanPlanNode() {
        super();
    }

    @Override
    public PlanNodeType getPlanNodeType() {
        return PlanNodeType.MATERIALIZEDSCAN;
    }

    public void setTableData(List<AbstractExpression> rows) {
        m_tableData.clear();
        m_tableData.addAll(rows);
    }

    public List<AbstractExpression> getTableData() {
        List<AbstractExpression> retval = new ArrayList<AbstractExpression>();
        retval.addAll(m_tableData);
        return retval;
    }

    /**
     * Accessor for flag marking the plan as guaranteeing an identical result/effect
     * when "replayed" against the same database state, such as during replication or CL recovery.
     * @return false
     */
    @Override
    public boolean isOrderDeterministic() {
        return true;
    }

    @Override
    public void computeCostEstimates(long childOutputTupleCountEstimate, Cluster cluster, Database db, DatabaseEstimates estimates, ScalarValueHints[] paramHints) {
        // assume constant cost. Most of the cost of the SQL-IN will be measured by the NLIJ that is always paired with this element
        m_estimatedProcessedTupleCount = 1;
        m_estimatedOutputTupleCount = 1;
    }

    @Override
    protected String explainPlanForNode(String indent) {
        return "MATERIALIZED SCAN of SQL-IN-LIST";
    }

    @Override
    public void resolveColumnIndexes() {
        // MaterializedScanPlanNodes have no children
        assert(m_children.size() == 0);
    }

    @Override
    public void toJSONString(JSONStringer stringer) throws JSONException {
        super.toJSONString(stringer);

        stringer.key(Members.TABLE_DATA.name());
        stringer.array();
        for (int i = 0; i < m_tableData.size(); i++) {
            stringer.object();
            AbstractExpression ae = m_tableData.get(i);
            assert(ae != null);
            ae.toJSONString(stringer);
            stringer.endObject();
        }
    }

    @Override
    protected void loadFromJSONObject(JSONObject obj, Database db) throws JSONException {
        helpLoadFromJSONObject(obj, db);

        assert(!obj.isNull(Members.TABLE_DATA.name()));

        //load table data
        JSONArray jarray = null;
        jarray = obj.getJSONArray(Members.TABLE_DATA.name());
        for (int i = 0; i < jarray.length(); i++) {
            AbstractExpression ae = AbstractExpression.fromJSONObject(obj, db);
            m_tableData.add(ae);
        }
    }
}
