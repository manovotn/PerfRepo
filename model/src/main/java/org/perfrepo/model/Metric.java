/**
 * PerfRepo
 * <p>
 * Copyright (C) 2015 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.perfrepo.model;

import org.perfrepo.model.auth.EntityType;
import org.perfrepo.model.auth.SecuredEntity;
import org.perfrepo.model.builder.MetricBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.util.Collection;

/**
 * Represents a test metric.
 *
 * @author Pavel Drozd (pdrozd@redhat.com)
 * @author Michal Linhard (mlinhard@redhat.com)
 */
@javax.persistence.Entity
@Table(name = "metric")
@NamedQueries({
    @NamedQuery(name = Metric.GET_TEST, query = "SELECT m.tests from Metric m where m= :entity"),
    @NamedQuery(name = Metric.FIND_BY_NAME_GROUPID, query = "SELECT m from Metric m inner join m.tests test where test.groupId= :groupId and m.name= :name"),
    @NamedQuery(name = Metric.FIND_BY_GROUPID, query = "SELECT DISTINCT m from Metric m inner join m.tests t WHERE t.groupId= :groupId ORDER BY m.name"),
})
@XmlRootElement(name = "metric")
@SecuredEntity(type = EntityType.TEST)
public class Metric implements Entity<Metric>, Comparable<Metric> {

   private static final long serialVersionUID = -5234628391341278215L;

   public static final String GET_TEST = "Metric.getTest";
   public static final String FIND_BY_NAME_GROUPID = "Metric.findByNameGroupId";
   public static final String FIND_BY_GROUPID = "Metric.findByGroupId";

   @Id
   @SequenceGenerator(name = "METRIC_ID_GENERATOR", sequenceName = "METRIC_SEQUENCE", allocationSize = 1)
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "METRIC_ID_GENERATOR")
   private Long id;

   @Column(name = "comparator")
   @NotNull(message = "{page.metric.comparatorRequired}")
   @Enumerated(EnumType.STRING)
   private MetricComparator comparator;

   @Column(name = "name")
   @NotNull(message = "{page.metric.nameRequired}")
   @Size(max = 2047)
   private String name;

   @OneToMany(mappedBy = "metric")
   private Collection<Value> values;

   @ManyToMany(mappedBy = "metrics")
   private Collection<Test> tests;

   @OneToMany(mappedBy = "metric")
   private Collection<Alert> alerts;

   @Column(name = "description")
   @Size(max = 10239)
   private String description;

   public Metric() {
      super();
   }

   @Override
   public Metric clone() {
      try {
         return (Metric) super.clone();
      } catch (CloneNotSupportedException e) {
         throw new RuntimeException(e);
      }
   }

   public Metric(String name, MetricComparator comparator, String description) {
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

   public Collection<Alert> getAlerts() {
      return alerts;
   }

   public void setAlerts(Collection<Alert> alerts) {
      this.alerts = alerts;
   }

   public void setStringId(String id) {
      this.id = Long.valueOf(id);
   }

   public void setComparator(MetricComparator comparator) {
      this.comparator = comparator;
   }

   @XmlAttribute(name = "comparator")
   public MetricComparator getComparator() {
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

   public void setTests(Collection<Test> tests) {
      this.tests = tests;
   }

   @Override
   public int compareTo(Metric o) {
      return this.getName().compareTo(o.getName());
   }

   @XmlElementWrapper(name = "tests")
   @XmlElement(name = "test")
   public Collection<Test> getTests() {
      return tests;
   }

   public static MetricBuilder builder() {
      return new MetricBuilder(null, new Metric());
   }
}