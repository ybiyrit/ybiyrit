<script>
  import { onMount } from 'svelte';
  
  const API_URL = '/api';
  let summary = null;
  let loading = true;
  let error = null;

  async function fetchSummary() {
    loading = true;
    error = null;
    
    try {
      const response = await fetch(`${API_URL}/summary`);
      
      if (!response.ok) {
        throw new Error('Failed to fetch summary');
      }
      
      summary = await response.json();
    } catch (err) {
      error = err.message;
    } finally {
      loading = false;
    }
  }

  onMount(() => {
    fetchSummary();
  });
</script>

<div class="summary">
  <h2>Carbon Footprint Summary</h2>
  
  {#if loading}
    <p class="loading">Loading...</p>
  {:else if error}
    <div class="error">
      {error}
      <button on:click={fetchSummary}>Retry</button>
    </div>
  {:else if summary}
    <div class="total">
      <h3>Total Carbon Footprint</h3>
      <p class="total-value">
        {summary.totalCarbonKg.toFixed(2)} kg CO₂
      </p>
      <p class="equivalent">
        ≈ {(summary.totalCarbonKg / 1000).toFixed(2)} tonnes CO₂
      </p>
    </div>

    {#if summary.recentCalculations && summary.recentCalculations.length > 0}
      <div class="recent">
        <h3>Recent Calculations</h3>
        <div class="calculations">
          {#each summary.recentCalculations as calc}
            <div class="calc-item">
              <div class="calc-header">
                <span class="category">{calc.category}</span>
                <span class="carbon">{calc.carbonKg.toFixed(2)} kg CO₂</span>
              </div>
              <p class="calc-details">
                {calc.value} {calc.unit} - {calc.activityType}
              </p>
            </div>
          {/each}
        </div>
      </div>
    {:else}
      <p class="no-data">No calculations yet. Start tracking your carbon footprint!</p>
    {/if}
  {/if}
</div>

<style>
  .summary {
    background: #f9f9f9;
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  h2 {
    margin-bottom: 1.5rem;
    color: #333;
  }

  .loading {
    text-align: center;
    padding: 2rem;
    color: #666;
  }

  .error {
    padding: 1rem;
    background: #ffebee;
    color: #c62828;
    border-radius: 8px;
    border-left: 4px solid #c62828;
  }

  .total {
    background: linear-gradient(135deg, #4CAF50, #45a049);
    color: white;
    padding: 2rem;
    border-radius: 12px;
    text-align: center;
    margin-bottom: 2rem;
  }

  .total h3 {
    margin-bottom: 1rem;
    font-size: 1.2rem;
  }

  .total-value {
    font-size: 3rem;
    font-weight: bold;
    margin-bottom: 0.5rem;
  }

  .equivalent {
    font-size: 1.1rem;
    opacity: 0.9;
  }

  .recent {
    margin-top: 2rem;
  }

  .recent h3 {
    margin-bottom: 1rem;
    color: #333;
  }

  .calculations {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }

  .calc-item {
    background: white;
    padding: 1rem;
    border-radius: 8px;
    border-left: 4px solid #4CAF50;
  }

  .calc-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 0.5rem;
  }

  .category {
    font-weight: 600;
    text-transform: capitalize;
    color: #4CAF50;
  }

  .carbon {
    font-weight: bold;
    color: #2e7d32;
  }

  .calc-details {
    color: #666;
    font-size: 0.9rem;
  }

  .no-data {
    text-align: center;
    padding: 2rem;
    color: #666;
  }

  @media (prefers-color-scheme: dark) {
    .summary {
      background: #2a2a2a;
    }

    h2, .recent h3 {
      color: #f6f6f6;
    }

    .error {
      background: #5d1f1f;
      color: #ffcdd2;
    }

    .calc-item {
      background: #3a3a3a;
    }

    .calc-details {
      color: #ccc;
    }

    .no-data, .loading {
      color: #aaa;
    }
  }
</style>
