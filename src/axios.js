import axios from 'axios';

const Axios = axios.create({
  baseURL: 'https://stroke-counter.firebaseio.com'
});

export default Axios;
