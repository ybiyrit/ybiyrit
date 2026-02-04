<script>
  import { onMount } from 'svelte';
  
  const API_URL = '/api';
  let calculations = [];
  let loading = true;
  let error = null;

  async function fetchHistory() {
    loading = true;
    error = null;
    
    try {
      const response = await fetch(`${API_URL}/calculations?limit=50`);
      
      if (!response.ok) {
        throw new Error('Failed to fetch history');
      }
      
      calculations = await response.json();
    } catch (err) {
      error = err.message;
    } finally {
      loading = false;
    }
  }

  onMount(() => {
    fetchHistory();
  });

  function formatDate(timestamp) {
    return new Date(timestamp).toLocaleString();
  }
</script>

<div class="history">
  <h2>Calculation History</h2>
  
  {#if loading}
    <p class="loading">Loading...</p>
  {:else if error}
    <div class="error">
      {error}
      <button on:click={fetchHistory}>Retry</button>
    </div>
  {:else if calculations.length > 0}
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>Date</th>
            <th>Category</th>
            <th>Activity</th>
            <th>Amount</th>
            <th>Carbon (kg CO₂)</th>
          </tr>
        </thead>
        <tbody>
          {#each calculations as calc}
            <tr>
              <td>{formatDate(calc.timestamp)}</td>
              <td class="category">{calc.category}</td>
              <td>{calc.activityType.replace(/_/g, ' ')}</td>
              <td>{calc.value} {calc.unit}</td>
              <td class="carbon">{calc.carbonKg.toFixed(2)}</td>
            </tr>
          {/each}
        </tbody>
      </table>
    </div>
  {:else}
    <p class="no-data">No calculations yet. Start tracking your carbon footprint!</p>
  {/if}
</div>

<style>
  .history {
    background: #f9f9f9;
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  h2 {
    margin-bottom: 1.5rem;
    color: #333;
  }

  .loading, .no-data {
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

  .table-container {
    overflow-x: auto;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    background: white;
    border-radius: 8px;
    overflow: hidden;
  }

  thead {
    background: #4CAF50;
    color: white;
  }

  th, td {
    padding: 1rem;
    text-align: left;
  }

  th {
    font-weight: 600;
  }

  tbody tr {
    border-bottom: 1px solid #eee;
  }

  tbody tr:hover {
    background: #f5f5f5;
  }

  .category {
    text-transform: capitalize;
    font-weight: 500;
  }

  .carbon {
    font-weight: 600;
    color: #4CAF50;
  }

  @media (prefers-color-scheme: dark) {
    .history {
      background: #2a2a2a;
    }

    h2 {
      color: #f6f6f6;
    }

    .loading, .no-data {
      color: #aaa;
    }

    .error {
      background: #5d1f1f;
      color: #ffcdd2;
    }

    table {
      background: #3a3a3a;
    }

    tbody tr {
      border-bottom-color: #555;
    }

    tbody tr:hover {
      background: #444;
    }

    td {
      color: #f6f6f6;
    }
  }

  @media (max-width: 768px) {
    table {
      font-size: 0.9rem;
    }

    th, td {
      padding: 0.75rem 0.5rem;
    }
  }
</style>
