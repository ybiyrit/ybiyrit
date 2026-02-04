<script>
  const API_URL = '/api';
  
  let category = 'transport';
  let activityType = 'car_petrol';
  let value = '';
  let unit = 'km';
  let result = null;
  let error = null;
  let loading = false;

  const categories = {
    transport: {
      label: 'Transportation',
      types: {
        car_petrol: { label: 'Car (Petrol)', unit: 'km' },
        car_diesel: { label: 'Car (Diesel)', unit: 'km' },
        car_electric: { label: 'Car (Electric)', unit: 'km' },
        bus: { label: 'Bus', unit: 'km' },
        train: { label: 'Train', unit: 'km' },
        flight_short: { label: 'Flight (Short distance)', unit: 'km' },
        flight_long: { label: 'Flight (Long distance)', unit: 'km' }
      }
    },
    energy: {
      label: 'Energy',
      types: {
        electricity: { label: 'Electricity', unit: 'kWh' },
        gas: { label: 'Natural Gas', unit: 'kWh' },
        heating_oil: { label: 'Heating Oil', unit: 'kWh' }
      }
    },
    food: {
      label: 'Food',
      types: {
        beef: { label: 'Beef', unit: 'kg' },
        lamb: { label: 'Lamb', unit: 'kg' },
        pork: { label: 'Pork', unit: 'kg' },
        chicken: { label: 'Chicken', unit: 'kg' },
        fish: { label: 'Fish', unit: 'kg' },
        cheese: { label: 'Cheese', unit: 'kg' },
        milk: { label: 'Milk', unit: 'L' },
        eggs: { label: 'Eggs', unit: 'kg' },
        rice: { label: 'Rice', unit: 'kg' },
        vegetables: { label: 'Vegetables', unit: 'kg' }
      }
    },
    waste: {
      label: 'Waste',
      types: {
        general: { label: 'General Waste', unit: 'kg' },
        plastic: { label: 'Plastic', unit: 'kg' },
        paper: { label: 'Paper', unit: 'kg' }
      }
    }
  };

  $: {
    const types = categories[category]?.types || {};
    const firstType = Object.keys(types)[0];
    if (firstType && !types[activityType]) {
      activityType = firstType;
    }
    unit = types[activityType]?.unit || '';
  }

  async function calculate() {
    if (!value || value <= 0) {
      error = 'Please enter a valid value';
      return;
    }

    loading = true;
    error = null;
    result = null;

    try {
      const response = await fetch(`${API_URL}/calculate`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          category,
          activityType,
          value: parseFloat(value),
          unit
        })
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || 'Calculation failed');
      }

      result = await response.json();
      value = '';
    } catch (err) {
      error = err.message;
    } finally {
      loading = false;
    }
  }
</script>

<div class="calculator">
  <h2>Calculate Your Carbon Footprint</h2>
  
  <form on:submit|preventDefault={calculate}>
    <div class="form-group">
      <label for="category">Category</label>
      <select id="category" bind:value={category}>
        {#each Object.entries(categories) as [key, cat]}
          <option value={key}>{cat.label}</option>
        {/each}
      </select>
    </div>

    <div class="form-group">
      <label for="activity">Activity Type</label>
      <select id="activity" bind:value={activityType}>
        {#each Object.entries(categories[category].types) as [key, type]}
          <option value={key}>{type.label}</option>
        {/each}
      </select>
    </div>

    <div class="form-group">
      <label for="value">Amount ({unit})</label>
      <input 
        id="value" 
        type="number" 
        bind:value={value} 
        placeholder="Enter amount"
        step="0.01"
        min="0"
      />
    </div>

    <button type="submit" disabled={loading}>
      {loading ? 'Calculating...' : 'Calculate'}
    </button>
  </form>

  {#if error}
    <div class="error">
      {error}
    </div>
  {/if}

  {#if result}
    <div class="result">
      <h3>Result</h3>
      <p class="carbon-value">
        {result.carbonKg.toFixed(2)} kg CO₂
      </p>
      <p class="details">
        {result.value} {result.unit} of {categories[result.category].types[result.activityType].label}
      </p>
      <p class="saved">✓ Saved to history</p>
    </div>
  {/if}
</div>

<style>
  .calculator {
    background: #f9f9f9;
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  h2 {
    margin-bottom: 1.5rem;
    color: #333;
  }

  form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }

  .form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }

  label {
    font-weight: 500;
    color: #555;
  }

  .error {
    margin-top: 1rem;
    padding: 1rem;
    background: #ffebee;
    color: #c62828;
    border-radius: 8px;
    border-left: 4px solid #c62828;
  }

  .result {
    margin-top: 1.5rem;
    padding: 1.5rem;
    background: #e8f5e9;
    border-radius: 8px;
    border-left: 4px solid #4CAF50;
  }

  .result h3 {
    margin-bottom: 1rem;
    color: #2e7d32;
  }

  .carbon-value {
    font-size: 2rem;
    font-weight: bold;
    color: #4CAF50;
    margin-bottom: 0.5rem;
  }

  .details {
    color: #555;
    margin-bottom: 0.5rem;
  }

  .saved {
    color: #2e7d32;
    font-size: 0.9rem;
  }

  @media (prefers-color-scheme: dark) {
    .calculator {
      background: #2a2a2a;
    }

    h2 {
      color: #f6f6f6;
    }

    label {
      color: #ccc;
    }

    .error {
      background: #5d1f1f;
      color: #ffcdd2;
    }

    .result {
      background: #1b3a1f;
    }

    .result h3 {
      color: #81c784;
    }

    .details {
      color: #ccc;
    }

    .saved {
      color: #81c784;
    }
  }
</style>
