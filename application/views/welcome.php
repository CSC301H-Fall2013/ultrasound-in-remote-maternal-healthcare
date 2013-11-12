Hi, <strong><?php echo $username; ?></strong>! You are logged in now. <?php echo anchor('/auth/logout/', 'Logout'); ?>
<p>
<?php if ($this->config->item('allow_registration', 'tank_auth')) echo anchor('/register/', 'Register New User'); ?>