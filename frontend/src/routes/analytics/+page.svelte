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
      const baseUrl = "http://localhost:8080";

      const response = await fetch(
              `${baseUrl}/api/v1/analytics/daily-counts?start=${startDate.toISOString()}&end=${endDate.toISOString()}`
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
      type: 'bar',
      data: {
        labels: analyticsData.map(item => item.id),
        datasets: [{
          label: 'Daily Events',
          data: analyticsData.map(item => item.count),
          backgroundColor: 'rgba(79, 70, 229, 0.7)',
          borderColor: '#4F46E5',
          borderWidth: 1,
          barPercentage: 0.3,
          categoryPercentage: 0.7
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: 'Daily Event Count',
            font: {
              size: 16
            }
          },
          tooltip: {
            callbacks: {
              label: function (context) {
                return `Count: ${context.raw}`;
              }
            }
          },
          legend: {
            display: false
          }
        },
        scales: {
          x: {
            grid: {
              display: false
            },
            ticks: {
              maxRotation: 45,
              minRotation: 45
            }
          },
          y: {
            beginAtZero: true,
            grid: {
              color: 'rgba(200, 200, 200, 0.5)'
            },
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
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  }

  .loading,
  .error {
    text-align: center;
    padding: 2rem;
    color: #4F46E5;
  }

  .error {
    color: #DC2626;
  }
</style>
