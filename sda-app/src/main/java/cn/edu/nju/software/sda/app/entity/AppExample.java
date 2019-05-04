package cn.edu.nju.software.sda.app.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AppExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("`name` is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("`name` is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("`name` =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("`name` <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("`name` >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("`name` >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("`name` <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("`name` <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("`name` like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("`name` not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("`name` in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("`name` not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("`name` between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("`name` not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andPathIsNull() {
            addCriterion("`path` is null");
            return (Criteria) this;
        }

        public Criteria andPathIsNotNull() {
            addCriterion("`path` is not null");
            return (Criteria) this;
        }

        public Criteria andPathEqualTo(String value) {
            addCriterion("`path` =", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotEqualTo(String value) {
            addCriterion("`path` <>", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathGreaterThan(String value) {
            addCriterion("`path` >", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathGreaterThanOrEqualTo(String value) {
            addCriterion("`path` >=", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathLessThan(String value) {
            addCriterion("`path` <", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathLessThanOrEqualTo(String value) {
            addCriterion("`path` <=", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathLike(String value) {
            addCriterion("`path` like", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotLike(String value) {
            addCriterion("`path` not like", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathIn(List<String> values) {
            addCriterion("`path` in", values, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotIn(List<String> values) {
            addCriterion("`path` not in", values, "path");
            return (Criteria) this;
        }

        public Criteria andPathBetween(String value1, String value2) {
            addCriterion("`path` between", value1, value2, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotBetween(String value1, String value2) {
            addCriterion("`path` not between", value1, value2, "path");
            return (Criteria) this;
        }

        public Criteria andCreatedatIsNull() {
            addCriterion("createdAt is null");
            return (Criteria) this;
        }

        public Criteria andCreatedatIsNotNull() {
            addCriterion("createdAt is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedatEqualTo(Date value) {
            addCriterion("createdAt =", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedatNotEqualTo(Date value) {
            addCriterion("createdAt <>", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedatGreaterThan(Date value) {
            addCriterion("createdAt >", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedatGreaterThanOrEqualTo(Date value) {
            addCriterion("createdAt >=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedatLessThan(Date value) {
            addCriterion("createdAt <", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedatLessThanOrEqualTo(Date value) {
            addCriterion("createdAt <=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedatIn(List<Date> values) {
            addCriterion("createdAt in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedatNotIn(List<Date> values) {
            addCriterion("createdAt not in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedatBetween(Date value1, Date value2) {
            addCriterion("createdAt between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedatNotBetween(Date value1, Date value2) {
            addCriterion("createdAt not between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedatIsNull() {
            addCriterion("updatedAt is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedatIsNotNull() {
            addCriterion("updatedAt is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedatEqualTo(Date value) {
            addCriterion("updatedAt =", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedatNotEqualTo(Date value) {
            addCriterion("updatedAt <>", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedatGreaterThan(Date value) {
            addCriterion("updatedAt >", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedatGreaterThanOrEqualTo(Date value) {
            addCriterion("updatedAt >=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedatLessThan(Date value) {
            addCriterion("updatedAt <", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedatLessThanOrEqualTo(Date value) {
            addCriterion("updatedAt <=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedatIn(List<Date> values) {
            addCriterion("updatedAt in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedatNotIn(List<Date> values) {
            addCriterion("updatedAt not in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedatBetween(Date value1, Date value2) {
            addCriterion("updatedAt between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedatNotBetween(Date value1, Date value2) {
            addCriterion("updatedAt not between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andNodenumberIsNull() {
            addCriterion("`nodeNumber` is null");
            return (Criteria) this;
        }

        public Criteria andNodenumberIsNotNull() {
            addCriterion("`nodeNumber` is not null");
            return (Criteria) this;
        }

        public Criteria andNodenumberEqualTo(Integer value) {
            addCriterion("`nodeNumber` =", value, "nodenumber");
            return (Criteria) this;
        }

        public Criteria andNodenumberNotEqualTo(Integer value) {
            addCriterion("`nodeNumber` <>", value, "nodenumber");
            return (Criteria) this;
        }

        public Criteria andNodenumberGreaterThan(Integer value) {
            addCriterion("`nodeNumber` >", value, "nodenumber");
            return (Criteria) this;
        }

        public Criteria andNodenumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("`nodeNumber` >=", value, "nodenumber");
            return (Criteria) this;
        }

        public Criteria andNodenumberLessThan(Integer value) {
            addCriterion("`nodeNumber` <", value, "nodenumber");
            return (Criteria) this;
        }

        public Criteria andNodenumberLessThanOrEqualTo(Integer value) {
            addCriterion("`nodeNumber` <=", value, "nodenumber");
            return (Criteria) this;
        }

        public Criteria andNodenumberIn(List<Integer> values) {
            addCriterion("`nodeNumber` in", values, "nodenumber");
            return (Criteria) this;
        }

        public Criteria andNodenumberNotIn(List<Integer> values) {
            addCriterion("`nodeNumber` not in", values, "nodenumber");
            return (Criteria) this;
        }

        public Criteria andNodenumberBetween(Integer value1, Integer value2) {
            addCriterion("`nodeNumber` between", value1, value2, "nodenumber");
            return (Criteria) this;
        }

        public Criteria andNodenumberNotBetween(Integer value1, Integer value2) {
            addCriterion("`nodeNumber` not between", value1, value2, "nodenumber");
            return (Criteria) this;
        }

        public Criteria andFlagIsNull() {
            addCriterion("flag is null");
            return (Criteria) this;
        }

        public Criteria andFlagIsNotNull() {
            addCriterion("flag is not null");
            return (Criteria) this;
        }

        public Criteria andFlagEqualTo(Integer value) {
            addCriterion("flag =", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagNotEqualTo(Integer value) {
            addCriterion("flag <>", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagGreaterThan(Integer value) {
            addCriterion("flag >", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("flag >=", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagLessThan(Integer value) {
            addCriterion("flag <", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagLessThanOrEqualTo(Integer value) {
            addCriterion("flag <=", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagIn(List<Integer> values) {
            addCriterion("flag in", values, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagNotIn(List<Integer> values) {
            addCriterion("flag not in", values, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagBetween(Integer value1, Integer value2) {
            addCriterion("flag between", value1, value2, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("flag not between", value1, value2, "flag");
            return (Criteria) this;
        }

        public Criteria andDescIsNull() {
            addCriterion("`desc` is null");
            return (Criteria) this;
        }

        public Criteria andDescIsNotNull() {
            addCriterion("`desc` is not null");
            return (Criteria) this;
        }

        public Criteria andDescEqualTo(String value) {
            addCriterion("`desc` =", value, "desc");
            return (Criteria) this;
        }

        public Criteria andDescNotEqualTo(String value) {
            addCriterion("`desc` <>", value, "desc");
            return (Criteria) this;
        }

        public Criteria andDescGreaterThan(String value) {
            addCriterion("`desc` >", value, "desc");
            return (Criteria) this;
        }

        public Criteria andDescGreaterThanOrEqualTo(String value) {
            addCriterion("`desc` >=", value, "desc");
            return (Criteria) this;
        }

        public Criteria andDescLessThan(String value) {
            addCriterion("`desc` <", value, "desc");
            return (Criteria) this;
        }

        public Criteria andDescLessThanOrEqualTo(String value) {
            addCriterion("`desc` <=", value, "desc");
            return (Criteria) this;
        }

        public Criteria andDescLike(String value) {
            addCriterion("`desc` like", value, "desc");
            return (Criteria) this;
        }

        public Criteria andDescNotLike(String value) {
            addCriterion("`desc` not like", value, "desc");
            return (Criteria) this;
        }

        public Criteria andDescIn(List<String> values) {
            addCriterion("`desc` in", values, "desc");
            return (Criteria) this;
        }

        public Criteria andDescNotIn(List<String> values) {
            addCriterion("`desc` not in", values, "desc");
            return (Criteria) this;
        }

        public Criteria andDescBetween(String value1, String value2) {
            addCriterion("`desc` between", value1, value2, "desc");
            return (Criteria) this;
        }

        public Criteria andDescNotBetween(String value1, String value2) {
            addCriterion("`desc` not between", value1, value2, "desc");
            return (Criteria) this;
        }

        public Criteria andClasscountIsNull() {
            addCriterion("classCount is null");
            return (Criteria) this;
        }

        public Criteria andClasscountIsNotNull() {
            addCriterion("classCount is not null");
            return (Criteria) this;
        }

        public Criteria andClasscountEqualTo(Integer value) {
            addCriterion("classCount =", value, "classcount");
            return (Criteria) this;
        }

        public Criteria andClasscountNotEqualTo(Integer value) {
            addCriterion("classCount <>", value, "classcount");
            return (Criteria) this;
        }

        public Criteria andClasscountGreaterThan(Integer value) {
            addCriterion("classCount >", value, "classcount");
            return (Criteria) this;
        }

        public Criteria andClasscountGreaterThanOrEqualTo(Integer value) {
            addCriterion("classCount >=", value, "classcount");
            return (Criteria) this;
        }

        public Criteria andClasscountLessThan(Integer value) {
            addCriterion("classCount <", value, "classcount");
            return (Criteria) this;
        }

        public Criteria andClasscountLessThanOrEqualTo(Integer value) {
            addCriterion("classCount <=", value, "classcount");
            return (Criteria) this;
        }

        public Criteria andClasscountIn(List<Integer> values) {
            addCriterion("classCount in", values, "classcount");
            return (Criteria) this;
        }

        public Criteria andClasscountNotIn(List<Integer> values) {
            addCriterion("classCount not in", values, "classcount");
            return (Criteria) this;
        }

        public Criteria andClasscountBetween(Integer value1, Integer value2) {
            addCriterion("classCount between", value1, value2, "classcount");
            return (Criteria) this;
        }

        public Criteria andClasscountNotBetween(Integer value1, Integer value2) {
            addCriterion("classCount not between", value1, value2, "classcount");
            return (Criteria) this;
        }

        public Criteria andInterfacecountIsNull() {
            addCriterion("interfaceCount is null");
            return (Criteria) this;
        }

        public Criteria andInterfacecountIsNotNull() {
            addCriterion("interfaceCount is not null");
            return (Criteria) this;
        }

        public Criteria andInterfacecountEqualTo(Integer value) {
            addCriterion("interfaceCount =", value, "interfacecount");
            return (Criteria) this;
        }

        public Criteria andInterfacecountNotEqualTo(Integer value) {
            addCriterion("interfaceCount <>", value, "interfacecount");
            return (Criteria) this;
        }

        public Criteria andInterfacecountGreaterThan(Integer value) {
            addCriterion("interfaceCount >", value, "interfacecount");
            return (Criteria) this;
        }

        public Criteria andInterfacecountGreaterThanOrEqualTo(Integer value) {
            addCriterion("interfaceCount >=", value, "interfacecount");
            return (Criteria) this;
        }

        public Criteria andInterfacecountLessThan(Integer value) {
            addCriterion("interfaceCount <", value, "interfacecount");
            return (Criteria) this;
        }

        public Criteria andInterfacecountLessThanOrEqualTo(Integer value) {
            addCriterion("interfaceCount <=", value, "interfacecount");
            return (Criteria) this;
        }

        public Criteria andInterfacecountIn(List<Integer> values) {
            addCriterion("interfaceCount in", values, "interfacecount");
            return (Criteria) this;
        }

        public Criteria andInterfacecountNotIn(List<Integer> values) {
            addCriterion("interfaceCount not in", values, "interfacecount");
            return (Criteria) this;
        }

        public Criteria andInterfacecountBetween(Integer value1, Integer value2) {
            addCriterion("interfaceCount between", value1, value2, "interfacecount");
            return (Criteria) this;
        }

        public Criteria andInterfacecountNotBetween(Integer value1, Integer value2) {
            addCriterion("interfaceCount not between", value1, value2, "interfacecount");
            return (Criteria) this;
        }

        public Criteria andFunctioncountIsNull() {
            addCriterion("functionCount is null");
            return (Criteria) this;
        }

        public Criteria andFunctioncountIsNotNull() {
            addCriterion("functionCount is not null");
            return (Criteria) this;
        }

        public Criteria andFunctioncountEqualTo(Integer value) {
            addCriterion("functionCount =", value, "functioncount");
            return (Criteria) this;
        }

        public Criteria andFunctioncountNotEqualTo(Integer value) {
            addCriterion("functionCount <>", value, "functioncount");
            return (Criteria) this;
        }

        public Criteria andFunctioncountGreaterThan(Integer value) {
            addCriterion("functionCount >", value, "functioncount");
            return (Criteria) this;
        }

        public Criteria andFunctioncountGreaterThanOrEqualTo(Integer value) {
            addCriterion("functionCount >=", value, "functioncount");
            return (Criteria) this;
        }

        public Criteria andFunctioncountLessThan(Integer value) {
            addCriterion("functionCount <", value, "functioncount");
            return (Criteria) this;
        }

        public Criteria andFunctioncountLessThanOrEqualTo(Integer value) {
            addCriterion("functionCount <=", value, "functioncount");
            return (Criteria) this;
        }

        public Criteria andFunctioncountIn(List<Integer> values) {
            addCriterion("functionCount in", values, "functioncount");
            return (Criteria) this;
        }

        public Criteria andFunctioncountNotIn(List<Integer> values) {
            addCriterion("functionCount not in", values, "functioncount");
            return (Criteria) this;
        }

        public Criteria andFunctioncountBetween(Integer value1, Integer value2) {
            addCriterion("functionCount between", value1, value2, "functioncount");
            return (Criteria) this;
        }

        public Criteria andFunctioncountNotBetween(Integer value1, Integer value2) {
            addCriterion("functionCount not between", value1, value2, "functioncount");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountIsNull() {
            addCriterion("interFaceFunctionCount is null");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountIsNotNull() {
            addCriterion("interFaceFunctionCount is not null");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountEqualTo(Integer value) {
            addCriterion("interFaceFunctionCount =", value, "interfacefunctioncount");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountNotEqualTo(Integer value) {
            addCriterion("interFaceFunctionCount <>", value, "interfacefunctioncount");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountGreaterThan(Integer value) {
            addCriterion("interFaceFunctionCount >", value, "interfacefunctioncount");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountGreaterThanOrEqualTo(Integer value) {
            addCriterion("interFaceFunctionCount >=", value, "interfacefunctioncount");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountLessThan(Integer value) {
            addCriterion("interFaceFunctionCount <", value, "interfacefunctioncount");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountLessThanOrEqualTo(Integer value) {
            addCriterion("interFaceFunctionCount <=", value, "interfacefunctioncount");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountIn(List<Integer> values) {
            addCriterion("interFaceFunctionCount in", values, "interfacefunctioncount");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountNotIn(List<Integer> values) {
            addCriterion("interFaceFunctionCount not in", values, "interfacefunctioncount");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountBetween(Integer value1, Integer value2) {
            addCriterion("interFaceFunctionCount between", value1, value2, "interfacefunctioncount");
            return (Criteria) this;
        }

        public Criteria andInterfacefunctioncountNotBetween(Integer value1, Integer value2) {
            addCriterion("interFaceFunctionCount not between", value1, value2, "interfacefunctioncount");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}