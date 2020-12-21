package net.cheetahead.fund.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Fund {

    @Id
    private String id;
    @Column(name = "parent_id")
    private String parentId;
    private String name;
    @Column(name = "company_id")
    private String companyId;
    private String type;
    @Column(name = "current_page")
    private int currentPage = 0;
    @Column(name = "average_rate_by_month")
    private BigDecimal averageRateByMonth;
    @Column(name = "average_rate_by_year")
    private BigDecimal averageRateByYear;
    @Column(name = "total_rate")
    private BigDecimal totalRate;
    @Column(name="statistic_due_date")
    private LocalDate statisticDueDate;

    public LocalDate getStatisticDueDate() {
        return statisticDueDate;
    }

    public void setStatisticDueDate(LocalDate statisticDueDate) {
        this.statisticDueDate = statisticDueDate;
    }

    public BigDecimal getAverageRateByMonth() {
        return averageRateByMonth;
    }

    public void setAverageRateByMonth(BigDecimal averageRateByMonth) {
        this.averageRateByMonth = averageRateByMonth;
    }

    public BigDecimal getAverageRateByYear() {
        return averageRateByYear;
    }

    public void setAverageRateByYear(BigDecimal averageRateByYear) {
        this.averageRateByYear = averageRateByYear;
    }

    public BigDecimal getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(BigDecimal totalRate) {
        this.totalRate = totalRate;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


}
