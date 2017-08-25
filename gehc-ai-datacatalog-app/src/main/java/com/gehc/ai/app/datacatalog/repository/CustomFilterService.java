package com.gehc.ai.app.datacatalog.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.ImageSeries_;


@Service
public class CustomFilterService {
	
	public static final String GE_CLASS_COUNTS = "SELECT count(distinct image_set) as image_count, CAST(single_class as CHAR(500)) FROM ( "
			 + " SELECT image_set, JSON_EXTRACT(item, CONCAT('$.properties.ge_class[', idx, ']')) AS single_class "
			 + " FROM Annotation JOIN (  SELECT  0 AS idx UNION "
			 + " SELECT  1 AS idx UNION "
			 + " SELECT  2 AS idx UNION "
			 + " SELECT  3 AS idx UNION "
			 + " SELECT  4 AS idx UNION "
			 + " SELECT  5 AS idx UNION "
			 + " SELECT  6 AS idx UNION "
			 + " SELECT  8 AS idx UNION "
			 + " SELECT  9 AS idx UNION "
			 + " SELECT  10 AS idx UNION "
			 + " SELECT  11 ) AS indices WHERE JSON_EXTRACT(item, CONCAT('$.properties.ge_class[', idx, ']')) IS NOT NULL "
			 + " ORDER BY id, idx) AS LABEL_JSON GROUP BY single_class";
	
	public static final String SIMPLE_JSON_QUERY = "SELECT CAST(JSON_EXTRACT(item, '$.properties.ge_class') AS CHAR(500)) FROM annotation";
	
	static final String GE_CLASS_QUERY = "select distinct im.id, p.patient_id, im.modality, im.anatomy from image_set im "
			+ "inner join annotation an "
			+ "on an.image_set=im.id "
			+ "inner join patient p "
			+ "on p.id = im.patient_dbid ";

	static final List<Object> GE_CLASS_LIST = new ArrayList<Object>();
	
	private static Logger logger = LoggerFactory.getLogger(CustomFilterService.class);

	
	@Autowired
	EntityManager em;
	
	public void getSelectedColumns(List<String> attrib) {
		logger.info("=========entity manager=========" + em);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		final Root<ImageSeries> root = cq.from(ImageSeries.class);
		cq.multiselect(root.get(ImageSeries_.id), root.get(ImageSeries_.orgId));
		List<Tuple> tupleResult = em.createQuery(cq).getResultList();
		
		for(int k = 0; k < tupleResult.size(); k++) {
			Tuple t = tupleResult.get(k);
			logger.info(t.get(1).toString());
		}
		logger.info("returns tuple =========" + tupleResult.size());
		

//		Query q = em.createNativeQuery(SIMPLE_JSON_QUERY);
//		List objList = q.getResultList();
		Query q = em.createNativeQuery(GE_CLASS_COUNTS);

		@SuppressWarnings("unchecked")
		List<Object[]> objList = q.getResultList();
		
		Map<BigInteger, String> filterMap = new HashMap<BigInteger, String>();
        objList.stream().forEach((record) -> {
            logger.info(record[0].toString() + ",......." + record[1].toString());
            GE_CLASS_LIST.add(record[1]);
        });
		
        StringBuilder buf = new StringBuilder();
        buf.append(GE_CLASS_QUERY);
        for (int k = 0; k < GE_CLASS_LIST.size(); k++) {
        	buf.append(k==0? "where " : "or ");
        	buf.append("JSON_CONTAINS(an.item, '" + GE_CLASS_LIST.get(k) + "', '$.properties.ge_class') ");
        }
        
        logger.info("query is " + buf);
        
		logger.info("result size " + objList.size());
	}
}
