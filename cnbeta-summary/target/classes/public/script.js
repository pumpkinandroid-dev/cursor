(() => {
  const listEl = document.getElementById('list');
  const homeEl = document.getElementById('home');
  const prevEl = document.getElementById('prev');
  const nextEl = document.getElementById('next');
  const pageTextEl = document.getElementById('pageText');

  const KEY = 'cnbeta_page';
  let page = parseInt(localStorage.getItem(KEY) || '1', 10);
  if (Number.isNaN(page) || page < 1) page = 1;

  async function load() {
    pageTextEl.textContent = `第 ${page} 页`;
    listEl.innerHTML = '加载中...';
    try {
      const res = await fetch(`/api/summary?n=${page}`);
      const data = await res.json();
      if (!Array.isArray(data)) {
        listEl.innerHTML = `<pre>${escapeHtml(JSON.stringify(data, null, 2))}</pre>`;
        return;
      }
      listEl.innerHTML = '';
      for (const item of data) {
        const wrap = document.createElement('div');
        wrap.style.marginBottom = '16px';

        const h = document.createElement('a');
        h.href = item.url;
        h.textContent = item.title || '(无标题)';
        h.style.display = 'block';
        h.style.fontSize = '30px';
        h.target = '_blank';
        wrap.appendChild(h);

        const note = document.createElement('div');
        note.textContent = item.note || '';
        note.style.fontSize = '10px';
        wrap.appendChild(note);

        const sum = document.createElement('div');
        sum.textContent = item.summary || '';
        sum.style.fontSize = '20px';
        wrap.appendChild(sum);

        const spacer = document.createElement('div');
        spacer.style.height = '2em';
        wrap.appendChild(spacer);

        listEl.appendChild(wrap);
      }
    } catch (e) {
      listEl.textContent = '加载失败';
    }
  }

  function saveAndLoad() {
    if (page < 1) page = 1;
    localStorage.setItem(KEY, String(page));
    load();
  }

  homeEl.addEventListener('click', (e) => { e.preventDefault(); page = 1; saveAndLoad(); });
  prevEl.addEventListener('click', (e) => { e.preventDefault(); page = Math.max(1, page - 1); saveAndLoad(); });
  nextEl.addEventListener('click', (e) => { e.preventDefault(); page = page + 1; saveAndLoad(); });

  function escapeHtml(s){
    return String(s).replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;','\'':'&#39;'}[c]));
  }

  load();
})();





