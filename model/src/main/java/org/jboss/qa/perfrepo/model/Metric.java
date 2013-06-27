/* 
 * Copyright 2013 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.qa.perfrepo.model;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents a test metric.
 * 
 * @author Pavel Drozd (pdrozd@redhat.com)
 * @author Michal Linhard (mlinhard@redhat.com)
 */
@Entity
@Table(name = "metric")
@NamedQueries({
      @NamedQuery(name = Metric.FIND_TEST_ID, query = "SELECT test from TestMetric tm inner join tm.test test inner join tm.metric m where m= :entity"),
      @NamedQuery(name = Metric.FIND_BY_NAME_GROUPID, query = "SELECT m from TestMetric tm inner join tm.metric m inner join tm.test test where test.groupId= :groupId and m.name= :name"),
      @NamedQuery(name = Metric.FIND_BY_GROUPID, query = "SELECT m from TestMetric tm inner join tm.metric m inner join tm.test test where test.groupId= :groupId group by m.id") })
@XmlRootElement(name = "metric")
@Named("metric")
@RequestScoped
public class Metric implements Serializable, Comparable<Metric> {

   private static final long serialVersionUID = -5234628391341278215L;

   public static final String FIND_TEST_ID = "Metric.findTestId";
   public static final String FIND_BY_NAME_GROUPID = "Metric.findByNameGroupId";
   public static final String FIND_BY_GROUPID = "Metric.findByGroupId";

   @Id
   @SequenceGenerator(name = "METRIC_ID_GENERATOR", sequenceName = "METRIC_SEQUENCE", allocationSize = 1)
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "METRIC_ID_GENERATOR")
   private Long id;

   @Column(name = "comparator")
   @Size(max = 255)
   private String comparator;

   @Column(name = "name")
   @NotNull
   @Size(max = 2047)
   private String name;

   @OneToMany(mappedBy = "metric")
   private Collection<Value> values;

   @OneToMany(mappedBy = "metric")
   private Collection<TestMetric> testMetrics;

   @Column(name = "description")
   @NotNull
   @Size(max = 10239)
   private String description;

   public Metric() {
      super();
   }

   public Metric(String name, String comparator, String description) {
      super();
      this.name = name;
      this.comparator = comparator;
      this.description = description;
   }

   @XmlTransient
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   @XmlID
   @XmlAttribute(name = "id")
   public String getStringId() {
      return id == null ? null : String.valueOf(id);
   }

   public void setStringId(String id) {
      this.id = Long.valueOf(id);
   }

   public void setComparator(String comparator) {
      this.comparator = comparator;
   }

   @XmlAttribute(name = "comparator")
   public String getComparator() {
      return this.comparator;
   }

   public void setName(String name) {
      this.name = name;
   }

   @XmlAttribute(name = "name")
   public String getName() {
      return this.name;
   }

   public void setValues(Collection<Value> values) {
      this.values = values;
   }

   @XmlTransient
   public Collection<Value> getValues() {
      return this.values;
   }

   @XmlElement(name = "description")
   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   @XmlTransient
   public Collection<TestMetric> getTestMetrics() {
      return testMetrics;
   }

   public void setTestMetrics(Collection<TestMetric> testMetrics) {
      this.testMetrics = testMetrics;
   }

   @Override
   public int compareTo(Metric o) {
      return this.getName().compareTo(o.getName());
   }

}