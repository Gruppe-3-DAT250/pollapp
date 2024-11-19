<!-- Page for viewing analyticx -->

<script>
  import { onMount } from 'svelte';
  import Chart from 'chart.js/auto';
  
  let chart;
  let chartCanvas;
  let events = [];
  let isLoading = true;
  let error = null;
  
  async function fetchDailyCounts() {
    try {
      const endDate = new Date();
      const startDate = new Date();
      startDate.setDate(startDate.getDate() - 30); 
      
      const response = await fetch(
        `/v1/api/analytics/daily-counts?start=${startDate.toISOString()}&end=${endDate.toISOString()}`
      );
      
      if (!response.ok) throw new Error('Failed to fetch analytics data');
      
      const data = await response.json();
      return data;
    } catch (err) {
      error = err.message;
      return [];
    } finally {
      isLoading = false;
    }
  }
  
  async function initializeChart() {
    const analyticsData = await fetchDailyCounts();
    
    if (chart) {
      chart.destroy();
    }
    
    const ctx = chartCanvas.getContext('2d');
    chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: analyticsData.map(item => item.id),
        datasets: [{
          label: 'Daily Events',
          data: analyticsData.map(item => item.count),
          borderColor: '#4F46E5',
          backgroundColor: 'rgba(79, 70, 229, 0.1)',
          tension: 0.4,
          fill: true
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: 'Daily Event Count'
          },
          legend: {
            position: 'bottom'
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              precision: 0
            }
          }
        }
      }
    });
  }
  
  onMount(() => {
    initializeChart();
  });
</script>

{#if isLoading}
  <div class="loading">Loading analytics data...</div>
{:else if error}
  <div class="error">{error}</div>
{:else}
  <div class="chart-container">
    <canvas bind:this={chartCanvas}></canvas>
  </div>
{/if}

<style>
  .chart-container {
    position: relative;
    height: 60vh;
    width: 80vw;
    margin: 2rem auto;
    padding: 1rem;
    background: white;
    border-radius: 0.5rem;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  }
  
  .loading, .error {
    text-align: center;
    padding: 2rem;
    color: #4F46E5;
  }
  
  .error {
    color: #DC2626;
  }
</style>