/* Frontend logic for CampusLost
   - Loads lost & found lists from backend API
   - Applies search filters and supports advanced search
   - Integrates with Spring Boot authentication
*/

const selectors = {
  lostGrid: document.getElementById('lostGrid'),
  foundGrid: document.getElementById('foundGrid'),
  searchInput: document.getElementById('searchInput'),
  searchBtn: document.getElementById('searchBtn'),
  categoryFilter: document.getElementById('categoryFilter'),
  applyFilters: document.getElementById('applyFilters'),
  clearFilters: document.getElementById('clearFilters'),
  recentOnly: document.getElementById('recentOnly'),
  statusRadios: document.getElementsByName('status'),
  yearSpan: document.getElementById('year')
};

if (selectors.yearSpan) selectors.yearSpan.textContent = new Date().getFullYear();

const API_BASE = 'http://localhost:8081';

// Helper function to get auth token
function getAuthToken() {
  return localStorage.getItem('authToken');
}

// Helper function to check if user is authenticated
function isAuthenticated() {
  return !!getAuthToken();
}

// --- helper render function ---
function renderCard(item){
  const container = document.createElement('div');
  container.className = 'item-card';
  const img = document.createElement('div');
  img.className = 'thumb';
  if (item.imageUrl){
    const imgel = document.createElement('img');
    imgel.src = item.imageUrl;
    imgel.alt = item.title || 'image';
    imgel.style.width = '100%';
    imgel.style.height = '100%';
    imgel.style.objectFit = 'cover';
    imgel.style.borderRadius = '8px';
    img.textContent = '';
    img.appendChild(imgel);
  } else {
    img.textContent = (item.title||'')[0] || '📦';
  }

  const meta = document.createElement('div');
  meta.className = 'meta';
  const title = document.createElement('h3');
  title.textContent = item.title || 'Untitled';
  const desc = document.createElement('p');
  desc.textContent = item.description || '';
  const row = document.createElement('div');
  row.className = 'meta-row';
  const status = document.createElement('div');
  status.className = 'badge';
  status.textContent = (item.status || '').toUpperCase();
  const loc = document.createElement('div');
  loc.textContent = item.location ? `📍 ${item.location}` : '';
  const contact = document.createElement('div');
  contact.textContent = item.userName ? `👤 ${item.userName}` : '';
  row.appendChild(status);
  row.appendChild(loc);
  row.appendChild(contact);

  meta.appendChild(title);
  meta.appendChild(desc);
  meta.appendChild(row);

  container.appendChild(img);
  container.appendChild(meta);

  return container;
}

// --- API fetch with fallback ---
async function fetchItems({status='', keyword='', category='', recent=false} = {}){
  try {
    let url = `${API_BASE}/api/items`;

    // Build query parameters
    const params = new URLSearchParams();
    if (status) params.set('status', status.toUpperCase());
    if (keyword) params.set('keyword', keyword);
    if (category) params.set('category', category);

    // Use filter endpoint for advanced search
    if (params.toString()) {
      url = `${API_BASE}/api/items/filter?${params.toString()}`;
    }

    // For recent items, use specific endpoint
    if (recent && !params.toString()) {
      url = `${API_BASE}/api/items/recent`;
    }

    const headers = {};
    const authToken = getAuthToken();
    if (authToken) {
      // Add Bearer prefix to token
      headers['Authorization'] = `Bearer ${authToken}`;
    }

    const res = await fetch(url, {
      headers: headers,
      cache: 'no-store'
    });

    if (!res.ok) throw new Error(`API error: ${res.status}`);
    const items = await res.json();

    // Additional client-side filtering for recent items if needed
    if (recent && params.toString()) {
      const weekAgo = new Date();
      weekAgo.setDate(weekAgo.getDate() - 7);
      return items.filter(item => {
        const itemDate = new Date(item.datePosted);
        return itemDate >= weekAgo;
      });
    }

    return items;
  } catch (err){
    console.warn('API fetch failed, using demo data', err);
    // fallback to demo data for development
    return getDemoItems()
      .filter(it => !status || it.status.toUpperCase() === status.toUpperCase())
      .filter(it => !category || (it.category && it.category.toLowerCase().includes(category.toLowerCase())))
      .filter(it => !keyword || (it.title + ' ' + (it.description||'')).toLowerCase().includes(keyword.toLowerCase()));
  }
}

function getDemoItems(){
  return [
    { id:1, title:'Blue Student ID', description:'ID with name Kaviyamani — lost near library', category:'Documents', status:'LOST', location:'Library', imageUrl:null, datePosted: new Date().toISOString(), userName: 'Demo User' },
    { id:2, title:'Black Wallet', description:'Contains student card — found near Canteen', category:'Accessories', status:'FOUND', location:'Canteen', imageUrl:null, datePosted: new Date().toISOString(), userName: 'Demo User' },
    { id:3, title:'Calculus Book', description:'Brown cover, 3rd edition', category:'Books', status:'LOST', location:'Block A', imageUrl:null, datePosted: new Date().toISOString(), userName: 'Demo User' }
  ];
}

// --- load and render functions ---
async function loadAndRender(status){
  const keyword = selectors.searchInput ? selectors.searchInput.value.trim() : '';
  const category = selectors.categoryFilter ? selectors.categoryFilter.value : '';
  const recent = selectors.recentOnly ? selectors.recentOnly.checked : false;

  const items = await fetchItems({status, keyword, category, recent});
  const grid = status.toUpperCase() === 'FOUND' ? selectors.foundGrid : selectors.lostGrid;

  if (!grid) return;

  grid.innerHTML = '';
  if (!items.length){
    const empty = document.createElement('div');
    empty.textContent = 'No items found.';
    empty.style.color = '#6b7280';
    empty.style.textAlign = 'center';
    empty.style.padding = '2rem';
    grid.appendChild(empty);
    return;
  }
  items.forEach(it => grid.appendChild(renderCard(it)));
}

// Function to post new item
async function postItem(itemData) {
  try {
    const authToken = getAuthToken();
    if (!authToken) {
      alert('Please login to post items');
      return false;
    }

    const response = await fetch(`${API_BASE}/api/items`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${authToken}`
      },
      body: JSON.stringify(itemData)
    });

    if (response.ok) {
      const newItem = await response.json();
      alert('Item posted successfully!');
      // Refresh the displays
      loadAndRender('lost');
      loadAndRender('found');
      return true;
    } else {
      const error = await response.text();
      alert('Failed to post item: ' + error);
      return false;
    }
  } catch (error) {
    console.error('Error posting item:', error);
    alert('Network error. Please try again.');
    return false;
  }
}

// --- event wiring ---
document.addEventListener('DOMContentLoaded', () => {
  // initial loads
  loadAndRender('lost');
  loadAndRender('found');

  // search functionality
  if (selectors.searchBtn) {
    selectors.searchBtn.addEventListener('click', () => {
      loadAndRender('lost');
      loadAndRender('found');
    });
  }

  if (selectors.searchInput) {
    selectors.searchInput.addEventListener('keyup', (e) => {
      if (e.key === 'Enter') {
        loadAndRender('lost');
        loadAndRender('found');
      }
    });
  }

  // filter functionality
  if (selectors.applyFilters) {
    selectors.applyFilters.addEventListener('click', () => {
      const checkedStatus = Array.from(selectors.statusRadios || []).find(r=>r.checked);
      if (checkedStatus && checkedStatus.value) {
        loadAndRender(checkedStatus.value);
      } else {
        loadAndRender('lost');
        loadAndRender('found');
      }
    });
  }

  if (selectors.clearFilters) {
    selectors.clearFilters.addEventListener('click', () => {
      if (selectors.searchInput) selectors.searchInput.value = '';
      if (selectors.categoryFilter) selectors.categoryFilter.value = '';
      if (selectors.recentOnly) selectors.recentOnly.checked = false;
      Array.from(selectors.statusRadios || []).forEach(r => r.checked = false);
      loadAndRender('lost');
      loadAndRender('found');
    });
  }
});
