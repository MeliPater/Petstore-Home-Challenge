import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

const statuses = ['approved', 'placed', 'delivered'];

const scenarios = {
    load: [
        { duration: '10m', target: 100 },
        { duration: '30m', target: 200 },
        { duration: '10m', target: 0 },
    ],
    stress: [
        { duration: '10m', target: 200 },
        { duration: '30m', target: 1000 },
        { duration: '10m', target: 0 },
    ],
    smoke: [
        { duration: '10s', target: 0 },
        { duration: '30s', target: 5 },
        { duration: '10s', target: 0 },
    ],
     peak: [
         { duration: '1m', target: 100 },
         { duration: '8m', target: 2000 },
         { duration: '2m', target: 0 },
     ]
};

const scenario = __ENV.SCENARIO || 'load';

export let options = {
    stages: scenarios[scenario] || scenarios.load,
    thresholds: {
        http_req_duration: ['p(95) < 500', 'p(90) < 800'],
        http_req_failed: ['rate < 0.01'],
    },
};

export default function () {
    let orderId = Date.now() * 100 + __VU + __ITER;
    let petId = randomIntBetween(1, 100);
    let quantity = randomIntBetween(1, 5);
    let shipDate = new Date().toISOString();
    let status = statuses[randomIntBetween(0, statuses.length - 1)];
    let complete = true;

    let url = 'http://localhost:8080/api/v3/store/order';
    let payload = JSON.stringify({
        id: orderId,
        petId: petId,
        quantity: quantity,
        shipDate: shipDate,
        status: status,
        complete: complete,
    });

    let params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    let response = http.post(url, payload, params);

    let success = check(response, {
        'is status 200': (r) => r.status === 200,
        'response time < 500ms': (r) => r.timings.duration < 500,
    });

    if (!success) {
        console.error(`❌ Request error: ${response.status} - ${response.body}`);
    }

    sleep(1);
}