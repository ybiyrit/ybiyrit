<script>
  import Calculator from './lib/Calculator.svelte';
  import Summary from './lib/Summary.svelte';
  import History from './lib/History.svelte';
  
  let activeTab = 'calculator';
</script>

<main>
  <header>
    <h1>🌍 Carbon Footprint Calculator</h1>
    <p>Track and reduce your environmental impact</p>
  </header>

  <nav>
    <button class:active={activeTab === 'calculator'} on:click={() => activeTab = 'calculator'}>
      Calculator
    </button>
    <button class:active={activeTab === 'summary'} on:click={() => activeTab = 'summary'}>
      Summary
    </button>
    <button class:active={activeTab === 'history'} on:click={() => activeTab = 'history'}>
      History
    </button>
  </nav>

  <div class="content">
    {#if activeTab === 'calculator'}
      <Calculator />
    {:else if activeTab === 'summary'}
      <Summary />
    {:else if activeTab === 'history'}
      <History />
    {/if}
  </div>
</main>

<style>
  main {
    padding: 1rem;
  }

  header {
    text-align: center;
    margin-bottom: 2rem;
  }

  header h1 {
    font-size: 2.5rem;
    margin-bottom: 0.5rem;
    color: #4CAF50;
  }

  header p {
    color: #666;
    font-size: 1.1rem;
  }

  nav {
    display: flex;
    gap: 1rem;
    justify-content: center;
    margin-bottom: 2rem;
    flex-wrap: wrap;
  }

  nav button {
    background-color: #f0f0f0;
    color: #333;
  }

  nav button.active {
    background-color: #4CAF50;
    color: white;
  }

  .content {
    max-width: 800px;
    margin: 0 auto;
  }

  @media (prefers-color-scheme: dark) {
    header p {
      color: #aaa;
    }
    
    nav button {
      background-color: #3f3f3f;
      color: #f6f6f6;
    }
    
    nav button.active {
      background-color: #4CAF50;
      color: white;
    }
  }
</style>
