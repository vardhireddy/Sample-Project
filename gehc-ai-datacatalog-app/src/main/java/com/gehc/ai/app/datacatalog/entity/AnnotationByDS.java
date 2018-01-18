package com.gehc.ai.app.datacatalog.entity;

public class AnnotationByDS {
		private static final long serialVersionUID = 1L;
		private String patientId;
		private String seriesInstanceUid;
		private Long annotationId;
	    private String type;
	    private String objectName;
	    private Object data;
	    private Object geClass;
		public String getPatientId() {
			return patientId;
		}
		public void setPatientId(String patientId) {
			this.patientId = patientId;
		}
		public String getSeriesInstanceUid() {
			return seriesInstanceUid;
		}
		public void setSeriesInstanceUid(String seriesInstanceUid) {
			this.seriesInstanceUid = seriesInstanceUid;
		}
		public Long getAnnotationId() {
			return annotationId;
		}
		public void setAnnotationId(Long annotationId) {
			this.annotationId = annotationId;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getObjectName() {
			return objectName;
		}
		public void setObjectName(String objectName) {
			this.objectName = objectName;
		}
		public Object getData() {
			return data;
		}
		public void setData(Object data) {
			this.data = data;
		}
		public Object getGeClass() {
			return geClass;
		}
		public void setGeClass(Object geClass) {
			this.geClass = geClass;
		}
		public AnnotationByDS(String patientId, String seriesInstanceUid, Long annotationId, String type,
				String objectName, Object data, Object geClass) {
			super();
			this.patientId = patientId;
			this.seriesInstanceUid = seriesInstanceUid;
			this.annotationId = annotationId;
			this.type = type;
			this.objectName = objectName;
			this.data = data;
			this.geClass = geClass;
		}
		public AnnotationByDS() {
			super();
			// TODO Auto-generated constructor stub
		}
}
