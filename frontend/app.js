
const IS_LOCAL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
const API_BASE = IS_LOCAL 
    ? 'http://localhost:8080/api/queue' 
    : 'https://java-dev-65kf.onrender.com'; 



const elements = {
    form: document.getElementById('issue-ticket-form'),
    nameInput: document.getElementById('customer-name'),
    typeSelect: document.getElementById('service-type'),
    btnIssue: document.getElementById('btn-issue'),
    alert: document.getElementById('new-ticket-alert'),
    statWaiting: document.getElementById('stat-waiting'),
    statServed: document.getElementById('stat-served'),
    btnRefresh: document.getElementById('btn-refresh-queues'),
    countersGrid: document.getElementById('counters-grid'),
    waitTimesList: document.getElementById('wait-times-list')
};


const queues = {
    PAYMENTS: { list: document.getElementById('list-payments'), badge: document.getElementById('badge-payments') },
    SUPPORT: { list: document.getElementById('list-support'), badge: document.getElementById('badge-support') },
    ACCOUNTS: { list: document.getElementById('list-accounts'), badge: document.getElementById('badge-accounts') }
};


document.addEventListener('DOMContentLoaded', () => {
    refreshAllData();
    setInterval(refreshAllData, 5000); // Auto-refresh every 5 seconds
});


elements.form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = elements.nameInput.value.trim();
    const type = elements.typeSelect.value;
    
    if(!name || !type) return;

    elements.btnIssue.disabled = true;
    elements.btnIssue.textContent = 'Issuing...';

    try {
        
        const params = new URLSearchParams();
        params.append('name', name);
        params.append('type', type);

        const response = await fetch(`${API_BASE}/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params
        });

        if(!response.ok) throw new Error('Failed to issue ticket');
        
        const ticket = await response.json();
        showAlert(`Success! Ticket ${ticket.ticketNumber} issued to ${ticket.customerName}.`);
        
        elements.form.reset();
        refreshAllData();
        
    } catch (error) {
        showAlert('Error issuing ticket. Check backend connection.', true);
        console.error(error);
    } finally {
        elements.btnIssue.disabled = false;
        elements.btnIssue.textContent = 'Get Ticket';
    }
});

elements.btnRefresh.addEventListener('click', () => {
    refreshAllData();
    elements.btnRefresh.style.transform = `rotate(${Math.random() * 360}deg)`;
});

async function refreshAllData() {
    fetchStats();
    fetchQueues();
    fetchCounters();
    fetchWaitTimes();
}

async function fetchStats() {
    try {
        const res = await fetch(`${API_BASE}/stats`);
        const stats = await res.json();
        updateCounterAnimation(elements.statWaiting, stats.customersWaiting);
        updateCounterAnimation(elements.statServed, stats.customersServed);
    } catch(e) { console.error('Error fetching stats:', e); }
}

async function fetchQueues() {
    try {
        const res = await fetch(`${API_BASE}/all`);
        const allQueues = await res.json();
        
        for (const [type, data] of Object.entries(queues)) {
            const queueItems = allQueues[type] || [];
            
            // Update counts
            data.badge.textContent = queueItems.length;
            
            // Build list
            data.list.innerHTML = '';
            queueItems.forEach(ticket => {
                const li = document.createElement('li');
                li.className = 'ticket-item';
                li.innerHTML = `
                    <span class="ticket-id">${ticket.ticketNumber}</span>
                    <span class="ticket-name">${ticket.customerName}</span>
                `;
                data.list.appendChild(li);
            });
        }
    } catch(e) { console.error('Error fetching queues:', e); }
}

async function fetchCounters() {
    try {
        const res = await fetch(`${API_BASE}/counters`);
        const counters = await res.json();
        
        elements.countersGrid.innerHTML = '';
        counters.forEach(counter => {
            const card = document.createElement('div');
            card.className = 'counter-card';
            
            const isIdle = !counter.currentTicket;
            
            card.innerHTML = `
                <div class="counter-header">
                    <span>Counter 0${counter.counterNumber}</span>
                    <span class="counter-type">${counter.serviceType}</span>
                </div>
                <div class="serving-now">
                    ${isIdle 
                        ? `<span class="serving-status idle">Idle</span>
                           <span class="serving-ticket">-</span>`
                        : `<span class="serving-status">Serving Now</span>
                           <span class="serving-ticket">${counter.currentTicket.ticketNumber}</span>
                           <span class="serving-name">${counter.currentTicket.customerName}</span>`
                    }
                </div>
                <button class="btn-action" onclick="callNextCustomer(${counter.counterNumber})">Call Next</button>
            `;
            elements.countersGrid.appendChild(card);
        });
    } catch(e) { console.error('Error fetching counters:', e); }
}

async function fetchWaitTimes() {
    try {
        const res = await fetch(`${API_BASE}/wait-times`);
        const waitTimes = await res.json();
        
        elements.waitTimesList.innerHTML = '';
        
        const keys = Object.keys(waitTimes);
        if(keys.length === 0) {
            elements.waitTimesList.innerHTML = '<p class="empty-state">No customers in queue</p>';
            return;
        }

        keys.forEach(ticketNumber => {
            const time = waitTimes[ticketNumber];
            const div = document.createElement('div');
            div.className = 'wait-time-tag';
            div.innerHTML = `
                <span class="id">${ticketNumber}</span>
                <span class="mins">Wait: ${time}m</span>
            `;
            elements.waitTimesList.appendChild(div);
        });

    } catch(e) { console.error('Error fetching wait times:', e); }
}

window.callNextCustomer = async (counterNumber) => {
    try {
        const response = await fetch(`${API_BASE}/call-counter/${counterNumber}`, {
            method: 'POST'
        });
        
        if(response.ok) {
            const ticket = await response.text();
            if(ticket) { 
                const tData = JSON.parse(ticket);
                showAlert(`Counter ${counterNumber} is now serving ${tData.ticketNumber}`, false);
            } else {
                showAlert(`Counter ${counterNumber}: No customers waiting.`, true);
            }
        }
        refreshAllData();
    } catch (e) {
        console.error('Error calling next customer:', e);
    }
}

function showAlert(msg, isError = false) {
    elements.alert.textContent = msg;
    elements.alert.className = `alert ${isError ? 'error' : ''}`;
    elements.alert.classList.remove('hidden');
    
    setTimeout(() => {
        elements.alert.classList.add('hidden');
    }, 4000);
}


function updateCounterAnimation(el, newValue) {
    if(el.textContent != newValue) {
        el.style.transform = 'scale(1.2) translateY(-2px)';
        el.style.color = '#ef4444'; 
        setTimeout(() => {
            el.textContent = newValue;
            el.style.transform = 'scale(1) translateY(0)';
            el.style.color = '';
        }, 150);
    } else {
        el.textContent = newValue;
    }
}
