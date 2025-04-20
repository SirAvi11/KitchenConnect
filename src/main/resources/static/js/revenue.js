/*<![CDATA[*/
// Global variables
let revenueChart;
let currentStartDate, currentEndDate;
let currentPeriod = 'weekly';
let userRole = document.getElementById("loggedInUser").value;
let role = userRole == 'ADMIN' ? 'platform' : "chef";

// Initialize the dashboard
document.addEventListener('DOMContentLoaded', function() {
    initializeDashboard();
    initializePaymentRecords();
    
    // Event listeners
    document.getElementById('periodSelect').addEventListener('change', function() {
        currentPeriod = this.value;
        resetDateRange();
        fetchRevenueData();
    });
    
    document.getElementById('prevPeriod').addEventListener('click', function() {
        navigatePeriod('prev');
    });
    
    document.getElementById('nextPeriod').addEventListener('click', function() {
        navigatePeriod('next');
    });
});

function initializeDashboard() {
    // Set initial period from dropdown
    currentPeriod = document.getElementById('periodSelect').value;
    resetDateRange();
    
    // Initialize chart
    const ctx = document.getElementById('revenueChart').getContext('2d');
    revenueChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: 'Revenue',
                data: [],
                borderColor: 'rgba(75, 192, 192, 1)',
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderWidth: 2,
                tension: 0.4,
                fill: true,
                pointBackgroundColor: 'rgba(75, 192, 192, 1)',
                pointRadius: 4,
                pointHoverRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false, // This gives you control over dimensions
            plugins: {
                legend: {
                    display: true,
                    position: 'top',
                    labels: {
                        font: {
                            size: 12,
                            family: "'Segoe UI', Roboto, sans-serif"
                        },
                        padding: 20
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.7)',
                    titleFont: {
                        size: 14
                    },
                    bodyFont: {
                        size: 12
                    },
                    padding: 10,
                    cornerRadius: 4,
                    displayColors: true,
                    callbacks: {
                        label: function(context) {
                            return '₹' + context.parsed.y.toLocaleString();
                        }
                    }
                }
            },
            scales: {
                x: {
                    type: 'time',
                    time: {
                        unit: 'day',
                        parser: 'YYYY-MM-DD',
                        tooltipFormat: 'MMM d, yyyy',
                        displayFormats: {
                            day: 'MMM d',
                            week: 'MMM d',
                            month: 'MMM yyyy',
                            year: 'yyyy'
                        }
                    },
                    grid: {
                        display: false,
                        drawBorder: false
                    },
                    ticks: {
                        font: {
                            size: 11,
                            family: "'Segoe UI', Roboto, sans-serif"
                        },
                        maxRotation: 45,
                        minRotation: 45,
                        padding: 10,
                        source: 'data'
                    }
                },
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.05)',
                        drawBorder: false
                    },
                    ticks: {
                        font: {
                            size: 11,
                            family: "'Segoe UI', Roboto, sans-serif"
                        },
                        padding: 10,
                        callback: function(value) {
                            return '₹' + value.toLocaleString();
                        }
                    }
                }
            },
            layout: {
                padding: {
                    top: 20,
                    right: 20,
                    bottom: 20,
                    left: 20
                }
            }
        }
    });
    
    // Fetch initial data
    fetchRevenueData();
}

function fetchRevenueData() {
    // Format dates for display
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    document.getElementById('dateRange').textContent = 
        currentStartDate.toLocaleDateString('en-US', options) + ' - ' + 
        currentEndDate.toLocaleDateString('en-US', options);

    // Format dates for API
    const startDateStr = formatDateForAPI(currentStartDate);
    const endDateStr = formatDateForAPI(currentEndDate);
    
    // Fetch data from backend
    fetch(`/revenue/${role}?period=${currentPeriod}&startDate=${startDateStr}&endDate=${endDateStr}`)
        .then(response => response.json())
        .then(data => {
            updateChart(data);
            updateMetrics(data);
        })
        .catch(error => console.error('Error fetching revenue data:', error));
}

function updateChart(data) {
    // Convert ISO strings to Date objects with proper time
    const dateObjects = data.labels.map(label => {
        // Add noon time to avoid timezone issues
        return new Date(label + 'T12:00:00');
    });

    // Update chart data
    revenueChart.data.labels = dateObjects;
    revenueChart.data.datasets[0].data = data.values;
    
    // Configure time scale based on period
    const timeScale = revenueChart.options.scales.x;
    timeScale.time.unit = currentPeriod === 'yearly' ? 'month' : 
                            currentPeriod === 'monthly' ? 'week' : 'day';
    
    // For weekly view, force daily ticks with proper formatting
    if (currentPeriod === 'weekly') {
        timeScale.time.displayFormats.day = 'MMM d'; // Format as "Mar 31"
        timeScale.ticks = {
            callback: function(value) {
                // Custom formatter to ensure proper dates
                const date = new Date(value);
                return date.toLocaleDateString('en-US', {
                    month: 'short',
                    day: 'numeric'
                });
            }
        };
    }
    
    // Set explicit bounds for the axis
    timeScale.min = dateObjects[0];
    timeScale.max = dateObjects[dateObjects.length - 1];
    
    revenueChart.update();
}

function updateMetrics(data) {
    // Update earnings
    document.getElementById('earningsValue').textContent = 
    '₹ ' + data.earnings.toFixed(2);
    
    // Update product sales
    document.getElementById('productSalesValue').innerHTML = 
        data.productSales.toLocaleString() + ' <span class="small text-muted">Items</span>';
    
    // Update visitors
    document.getElementById('visitorsValue').innerHTML = 
        data.visitors.toLocaleString() + ' <span class="small text-muted">People</span>';
    
    // Update trends
    updateTrendIndicator(
        'earningsTrend', 
        'earningsComparison', 
        data.percentageChange, 
        data.isIncrease,
        'Earnings'
    );
    
    // Note: You might want to add separate percentage changes for sales and visitors
    // For now using the same as earnings
    updateTrendIndicator(
        'salesTrend', 
        'salesComparison', 
        data.percentageChange, 
        data.isIncrease,
        'Sales'
    );
    
    updateTrendIndicator(
        'visitorsTrend', 
        'visitorsComparison', 
        data.percentageChange, 
        data.isIncrease,
        'Visitors'
    );
}

function resetDateRange() {
    const today = new Date();
    if (currentPeriod === 'weekly') {
        // Start from Monday of current week
        const day = today.getDay();
        const diff = today.getDate() - day + (day === 0 ? -6 : 1); // adjust when day is Sunday
        const monday = new Date(today.setDate(diff));
        currentStartDate = new Date(monday);
        currentEndDate = new Date(monday);
        currentEndDate.setDate(monday.getDate() + 6);
    } else if (currentPeriod === 'monthly') {
        // Start from first day of current month
        currentStartDate = new Date(today.getFullYear(), today.getMonth(), 1);
        currentEndDate = new Date(today.getFullYear(), today.getMonth() + 1, 0);
    } else { // yearly
        // Start from first day of current year
        currentStartDate = new Date(today.getFullYear(), 0, 1);
        currentEndDate = new Date(today.getFullYear(), 11, 31);
    }
}

function navigatePeriod(direction) {
    const increment = direction === 'next' ? 1 : -1;
    
    if (currentPeriod === 'weekly') {
        currentStartDate.setDate(currentStartDate.getDate() + (7 * increment));
        currentEndDate.setDate(currentEndDate.getDate() + (7 * increment));
    } else if (currentPeriod === 'monthly') {
        currentStartDate.setMonth(currentStartDate.getMonth() + increment);
        currentEndDate = new Date(
            currentStartDate.getFullYear(), 
            currentStartDate.getMonth() + 1, 
            0
        );
    } else { // yearly
        currentStartDate.setFullYear(currentStartDate.getFullYear() + increment);
        currentEndDate = new Date(currentStartDate.getFullYear(), 11, 31);
    }
    
    fetchRevenueData();
    fetchPaymentRecords();
}

function formatDateForAPI(date) {
    return date.toISOString().split('T')[0];
}

function updateTrendIndicator(elementId, comparisonId, percentage, isIncrease, metricName) {
    const trendElement = document.getElementById(elementId);
    const comparisonElement = document.getElementById(comparisonId);
    
    trendElement.className = `py-1 px-2 rounded-full small ${
        isIncrease ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
    }`;
    trendElement.textContent = isIncrease ? `↑ ${percentage}%` : `↓ ${Math.abs(percentage)}%`;
    
    comparisonElement.textContent = isIncrease 
        ? `Higher than last ${currentPeriod.slice(0, -2)}` 
        : `Lower than last ${currentPeriod.slice(0, -2)}`;
}

function initializePaymentRecords() {
    fetchPaymentRecords();
}

function fetchPaymentRecords() {
    const startDateStr = formatDateForAPI(currentStartDate);
    const endDateStr = formatDateForAPI(currentEndDate);
    
    fetch(`/revenue/${role}-payments?startDate=${startDateStr}&endDate=${endDateStr}`)
        .then(response => response.json())
        .then(data => {
            renderPaymentRecords(data);
        })
        .catch(error => console.error('Error fetching payment records:', error));
}

function renderPaymentRecords(payments) {
    const tbody = document.querySelector('#paymentRecordsTable tbody');
    tbody.innerHTML = '';
    
    if (payments.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" class="text-center py-4 text-muted">
                    No payment records found for the selected period
                </td>
            </tr>
        `;
        document.getElementById('recordsInfo').textContent = 'No records found';
        return;
    }
    
    payments.forEach(payment => {
        const row = document.createElement('tr');
        const statusClass = `status-${payment.status.toLowerCase()}`;
        const formattedDate = new Date(payment.paymentDate).toLocaleDateString('en-US', {
            month: 'short',
            day: 'numeric',
            year: 'numeric'
        });
        
        row.innerHTML = `
            <td>${formattedDate}</td>
            <td>${payment.orderId}</td>
            <td>₹${payment.chefAmount.toFixed(2)}</td>
            <td>₹${payment.platformFee.toFixed(2)}</td>
            <td>₹${payment.amount.toFixed(2)}</td>
            <td><span class="status-badge ${statusClass}">${payment.status}</span></td>
        `;
        
        tbody.appendChild(row);
    });
    
    document.getElementById('recordsInfo').textContent = 
        `Showing ${payments.length} records for selected period`;
}
/*]]>*/
