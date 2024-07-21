import http from 'k6/http';
import { check } from 'k6';

export let options = {
  stages: [
    { duration: '10m', target: 100000 },
  ],
  thresholds: {
    http_req_failed: ['rate<=0.01'],
  },
};

export default function () {
  let res = http.get('http://localhost:8080/medico/1');
  check(res, {
    'status was 200': (r) => r.status == 200,
  });
}
